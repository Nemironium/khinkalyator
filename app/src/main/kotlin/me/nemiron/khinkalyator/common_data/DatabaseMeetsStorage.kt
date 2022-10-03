package me.nemiron.khinkalyator.common_data

import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.nemiron.khinkalyator.KhinkalytorDatabase
import me.nemiron.khinkalyator.common_data.mapper.MeetOrder
import me.nemiron.khinkalyator.common_data.mapper.getPeopleOrders
import me.nemiron.khinkalyator.common_data.mapper.getTipsTypeValue
import me.nemiron.khinkalyator.common_data.mapper.getTypeAndValue
import me.nemiron.khinkalyator.common_data.mapper.mapToPerson
import me.nemiron.khinkalyator.common_data.mapper.mapToMeetStatus
import me.nemiron.khinkalyator.common_data.mapper.mapToTips
import me.nemiron.khinkalyator.common_data.mapper.toRestaurants
import me.nemiron.khinkalyator.common_data.utils.dbCall
import me.nemiron.khinkalyator.common_data.utils.suspendDbCall
import me.nemiron.khinkalyator.common_domain.model.Dish
import me.nemiron.khinkalyator.common_domain.model.MeetId
import me.nemiron.khinkalyator.common_domain.model.Meet
import me.nemiron.khinkalyator.common_domain.MeetsStorage
import me.nemiron.khinkalyator.common_domain.model.Order
import me.nemiron.khinkalyator.common_domain.model.OrderId
import me.nemiron.khinkalyator.common_domain.model.Person
import me.nemiron.khinkalyator.common_domain.model.PersonId
import me.nemiron.khinkalyator.common_domain.model.Restaurant
import me.nemiron.khinkalyator.common_domain.model.RestaurantId
import me.nemiron.khinkalyator.common_domain.model.code

// TODO: think about reusing storages (with non-suspend methods)
class DatabaseMeetsStorage(
    db: KhinkalytorDatabase,
    private val coroutineDispatcher: CoroutineDispatcher
) : MeetsStorage {

    private val meetQueries = db.meetQueries
    private val orderQueries = db.orderQueries
    private val personQueries = db.personQueries
    private val restaurantQueries = db.restaurantQueries
    private val dishQueries = db.dishQueries

    override fun observeMeets() = dbCall {
        meetQueries
            .selectAllMeets()
            .asFlow()
            .map { it.executeAsList() }
            .map { meetEntities ->
                val allRestaurants = getAllRestaurants()
                val allMeetPeople = getAllMeetPeople()
                val allMeetDishes = meetEntities
                    .map { it.restaurantId }
                    .associateWith { meetRestaurantId ->
                        allRestaurants.first { it.id == meetRestaurantId }.dishes
                    }
                val allMeetsOrders = getAllMeetOrders(allMeetPeople, allMeetDishes)

                meetEntities.map { meetEntity ->
                    val meetId = meetEntity.id
                    val restaurant = allRestaurants.first { it.id == meetEntity.restaurantId }
                    val meetPeople = allMeetPeople[meetId].orEmpty()
                    val meetOrders = allMeetsOrders[meetId].orEmpty()
                    val orders = getPeopleOrders(meetOrders)
                    val status = mapToMeetStatus(meetEntity.createDate, meetEntity.type)
                    val tips = mapToTips(meetEntity.tipsType, meetEntity.tipsValue)

                    Meet(meetId, restaurant, meetPeople, orders, status, tips)
                }
            }
            .flowOn(coroutineDispatcher)
    }

    override fun observeMeet(meetId: MeetId) = dbCall {
        meetQueries
            .selectMeetById(meetId)
            .asFlow()
            .map { it.executeAsOneOrNull() }
            .filterNotNull()
            .map { meetEntity ->
                val meetPeople = getMeetPeople(meetId)
                val restaurant = getMeetRestaurant(meetEntity.restaurantId)
                val meetOrders = getMeetOrders(meetId, meetPeople, restaurant.dishes)
                val orders = getPeopleOrders(meetOrders)
                val status = mapToMeetStatus(meetEntity.createDate, meetEntity.type)
                val tips = mapToTips(meetEntity.tipsType, meetEntity.tipsValue)
                Meet(meetId, restaurant, meetPeople, orders, status, tips)
            }.flowOn(coroutineDispatcher)
    }

    override suspend fun createMeet(
        restaurantId: RestaurantId,
        personIds: List<PersonId>,
        status: Meet.Status
    ): MeetId {
        return suspendDbCall {
            withContext(coroutineDispatcher) {
                meetQueries.transactionWithResult {
                    val (tipsType, tipsValue) = getTipsTypeValue(tips = null)
                    meetQueries.insertMeet(
                        id = null,
                        restaurantId = restaurantId,
                        createDate = status.createTime.toString(),
                        tipsType = tipsType,
                        tipsValue = tipsValue,
                        type = status.code
                    )
                    val meetId = meetQueries.getLastInsertedId().executeAsOne()

                    personIds.forEach { personId ->
                        meetQueries.insertMeetPerson(meetId, personId)
                    }
                    meetId
                }
            }
        }
    }

    override suspend fun getMeet(meetId: MeetId): Meet {
        return suspendDbCall {
            withContext(coroutineDispatcher) {
                val meetEntity = meetQueries.selectMeetById(meetId).executeAsOne()
                val restaurant = getMeetRestaurant(meetEntity.restaurantId)
                val meetPeople = getMeetPeople(meetId)
                val status = mapToMeetStatus(meetEntity.createDate, meetEntity.type)
                val meetOrders = getMeetOrders(meetId, meetPeople, restaurant.dishes)
                val orders = getPeopleOrders(meetOrders)
                val tips = mapToTips(meetEntity.tipsType, meetEntity.tipsValue)

                Meet(meetId, restaurant, meetPeople, orders, status, tips)
            }
        }
    }

    override suspend fun addOrder(meetId: MeetId, personId: PersonId, order: Order) {
        suspendDbCall {
            withContext(coroutineDispatcher) {
                orderQueries.transaction {
                    // FIXME+TODO: try to get existing order from DB by id (for cases when increment existing order entry
                    orderQueries.insertOrder(meetId, order.dish.id, order.quantity.count)
                    val orderId = orderQueries.getLastInsertedId().executeAsOne()

                    orderQueries.addOrderToPerson(personId, orderId)

                    // FIXME: проверь, что это не упадёт, когда уже будет добавлен заказ для человека
                    if (order.quantity is Order.DishQuantity.Shared) {
                        order.quantity.people.forEach { person ->
                            orderQueries.addOrderToPerson(person.id, orderId)
                        }
                    }
                }
            }
        }
    }

    override suspend fun archiveMeet(meetId: MeetId, tips: Meet.Tips?) {
        suspendDbCall {
            withContext(coroutineDispatcher) {
                val meet = getMeet(meetId)
                val meetPeople = meet.people
                val meetRestaurant = meet.restaurant

                meetQueries.transaction {
                    val archivedRestaurant = addArchivedRestaurant(meetRestaurant)
                    val archivedPeople = addArchivedPeople(meetPeople)
                    val archivedMeet = addArchivedMeet(meet, archivedRestaurant, tips)

                    addArchivedMeetPeople(archivedMeet.id, archivedPeople)

                    addArchivedOrders(
                        archivedMeet.id,
                        getActiveArchivedMap(meetPeople, archivedPeople),
                        getActiveArchivedMap(meetRestaurant.dishes, archivedRestaurant.dishes),
                        meet.orders
                    )

                    meetQueries.delete(meetId)
                }
            }
        }
    }

    override suspend fun deleteMeet(meetId: MeetId) {
        suspendDbCall {
            withContext(coroutineDispatcher) {
                val meet = getMeet(meetId)
                meetQueries.transaction {
                    meetQueries.delete(meetId)

                    // delete archived extra records
                    when (meet.status) {
                        is Meet.Status.Active -> {
                            // nothing
                        }
                        is Meet.Status.Archived -> {
                            meet.people.forEach { person ->
                                personQueries.delete(person.id)
                            }
                            restaurantQueries.delete(meet.restaurant.id)
                        }
                    }
                }
            }
        }
    }

    override suspend fun isPersonAttemptsInAnyMeet(personId: PersonId): Boolean {
        return suspendDbCall {
            withContext(coroutineDispatcher) {
                meetQueries.isPersonAttemptsInAnyMeet(personId).executeAsOne()
            }
        }
    }

    override suspend fun isRestaurantAttemptsInAnyMeet(restaurantId: RestaurantId): Boolean {
        return suspendDbCall {
            withContext(coroutineDispatcher) {
                meetQueries.isRestaurantAttemptsInAnyMeet(restaurantId).executeAsOne()
            }
        }
    }

    private fun getMeetRestaurant(id: RestaurantId) =
        restaurantQueries.selectByIdWithDishes(id)
            .executeAsList()
            .toRestaurants()
            .first()

    private fun getAllRestaurants() =
        restaurantQueries.selectAllWithDishes()
            .executeAsList()
            .toRestaurants()

    private fun getMeetPeople(meetId: MeetId) = meetQueries
        .selectMeetPeopleById(meetId)
        .executeAsList()
        .map { mapToPerson(it.personId, it.name, it.emoji, it.phone, it.type) }

    private fun getAllMeetPeople() = meetQueries
        .selectAllMeetsPeople()
        .executeAsList()
        .groupBy { it.meetId }
        .mapValues { (_, meetEntities) ->
            meetEntities.map {
                mapToPerson(it.personId, it.name, it.emoji, it.phone, it.type)
            }
        }

    private fun getMeetOrders(
        id: MeetId,
        meetPeople: List<Person>,
        meetDishes: List<Dish>
    ) = orderQueries
        .selectMeetsOrdersById(id)
        .executeAsList()
        .map { personOrderEntity ->
            val dish = meetDishes.first { it.id == personOrderEntity.dishId }
            val person = meetPeople.first { it.id == personOrderEntity.personId }
            MeetOrder(personOrderEntity.orderId, dish, person, personOrderEntity.count)
        }

    private fun getAllMeetOrders(
        allMeetPeople: Map<MeetId, List<Person>>,
        allMeetDishes: Map<MeetId, List<Dish>>
    ) = orderQueries
        .selectAllMeetsOrders()
        .executeAsList()
        .groupBy { it.meetId }
        .mapValues { (meetId, personOrderEntities) ->
            val meetPeople = allMeetPeople[meetId].orEmpty()
            val meetDishes = allMeetDishes[meetId].orEmpty()
            personOrderEntities.map { personOrderEntity ->
                val person = meetPeople.first { it.id == personOrderEntity.personId }
                val dish = meetDishes.first { it.id == personOrderEntity.dishId }
                MeetOrder(personOrderEntity.orderId, dish, person, personOrderEntity.count)
            }
        }

    private fun addArchivedMeet(meet: Meet, restaurant: Restaurant, tips: Meet.Tips?): Meet {
        var archivedMeet = meet.copy(
            status = Meet.Status.Archived(meet.status.createTime),
            restaurant = restaurant
        )
        tips?.let { archivedMeet = archivedMeet.copy(tips = tips) }

        val (tipsType, tipsValue) = getTipsTypeValue(archivedMeet.tips)

        meetQueries.insertMeet(
            id = null,
            restaurantId = archivedMeet.restaurant.id,
            createDate = archivedMeet.status.createTime.toString(),
            tipsType = tipsType,
            tipsValue = tipsValue,
            type = archivedMeet.status.code
        )
        val meetId = meetQueries.getLastInsertedId().executeAsOne()

        return archivedMeet.copy(id = meetId)
    }

    private fun addArchivedRestaurant(restaurant: Restaurant): Restaurant {
        var archivedRestaurant = restaurant.copy(status = Restaurant.Status.Archived)

        restaurantQueries.insertOrReplace(
            id = null,
            name = archivedRestaurant.name,
            address = archivedRestaurant.address?.value,
            phone = archivedRestaurant.phone?.value,
            type = archivedRestaurant.status.code
        )
        val archivedRestaurantId = restaurantQueries.getLastInsertedId().executeAsOne()
        archivedRestaurant = archivedRestaurant.copy(id = archivedRestaurantId)

        val archivedDishes = archivedRestaurant.dishes.map { dish ->
            val (discountType, discountValue) = dish.discount.getTypeAndValue()
            dishQueries.insertOrReplace(
                id = null,
                restaurantId = archivedRestaurantId,
                name = dish.name,
                price = dish.price.value,
                discountType = discountType,
                discountValue = discountValue,
                type = Dish.Status.Archived.code
            )
            val archivedDishId = dishQueries.getLastInsertedId().executeAsOne()
            dish.copy(id = archivedDishId, status = Dish.Status.Archived)
        }
        archivedRestaurant = archivedRestaurant.copy(dishes = archivedDishes)

        return archivedRestaurant
    }

    private fun addArchivedPeople(people: List<Person>): List<Person> {
        return people
            .map { it.copy(status = Person.Status.Archived) }
            .map { person ->
                val archivedPerson = person.copy(status = Person.Status.Archived)
                personQueries.insertOrReplace(
                    id = null,
                    name = person.name,
                    phone = person.phone?.value,
                    emoji = person.emoji.value,
                    type = person.status.code
                )
                val archivedPersonId = personQueries.getLastInsertedId().executeAsOne()
                archivedPerson.copy(id = archivedPersonId)
            }
    }

    private fun addArchivedMeetPeople(archivedMeetId: MeetId, archivedPeople: List<Person>) {
        archivedPeople.forEach { person ->
            meetQueries.insertMeetPerson(archivedMeetId, person.id)
        }
    }

    private fun addArchivedOrders(
        archivedMeetId: MeetId,
        activeArchivedPersonMap: Map<Person, Person>,
        activeArchivedDishMap: Map<Dish, Dish>,
        orders: Map<Person, List<Order>>
    ): Map<Person, List<Order>> {
        // prevent adding extra order entry for Order.DishQuantity.Shared
        val activeArchivedOrderIdMap = mutableMapOf<OrderId, OrderId>()

        return orders
            .mapKeys { (person, _) ->
                activeArchivedPersonMap.getValue(person)
            }
            .mapValues { (archivedPerson, orders) ->

                orders.map { order ->
                    val archivedDish = activeArchivedDishMap.getValue(order.dish)

                    val archivedOrderId = activeArchivedOrderIdMap[order.id].let {
                        if (it == null) {
                            orderQueries.insertOrder(
                                archivedMeetId,
                                archivedDish.id,
                                order.quantity.count
                            )
                            val archivedOrderId = orderQueries.getLastInsertedId().executeAsOne()
                            activeArchivedOrderIdMap[order.id] = archivedOrderId
                            archivedOrderId
                        } else {
                            it
                        }
                    }

                    orderQueries.addOrderToPerson(archivedPerson.id, archivedOrderId)

                    val archivedQuantity = when (order.quantity) {
                        is Order.DishQuantity.Individual -> {
                            order.quantity
                        }
                        is Order.DishQuantity.Shared -> {
                            val archivedSharedPeople =
                                order.quantity.people.map(activeArchivedPersonMap::getValue)
                            order.quantity.copy(people = archivedSharedPeople)
                        }
                    }

                    Order(
                        id = archivedOrderId,
                        dish = archivedDish,
                        quantity = archivedQuantity
                    )
                }
            }
    }

    private fun <T> getActiveArchivedMap(
        activeEntries: List<T>,
        archivedEntries: List<T>
    ): Map<T, T> {
        val buffer = buildMap {
            archivedEntries.forEachIndexed { index, archivedEntry ->
                put(index, archivedEntry)
            }
        }
        return buildMap {
            activeEntries.forEachIndexed { index, activeEntry ->
                put(activeEntry, buffer.getValue(index))
            }
        }
    }
}