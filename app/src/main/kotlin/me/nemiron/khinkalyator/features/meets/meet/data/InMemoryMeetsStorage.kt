package me.nemiron.khinkalyator.features.meets.meet.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.meets.meet.domain.Meet
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetId
import me.nemiron.khinkalyator.features.meets.meet.domain.MeetsStorage
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Restaurant
import kotlin.random.Random
import kotlin.time.Duration.Companion.days

class InMemoryMeetsStorage : MeetsStorage {

    private val mockedRestaurants = listOf(
        Restaurant(
            id = Random.nextLong(),
            name = "Каха бар",
            address = Address("ул. Рубинштейна, 24"),
            phone = Phone("89219650524"),
            menu = emptyList()
        ),
        Restaurant(
            id = Random.nextLong(),
            name = "Каха бар",
            address = Address("Большой проспект П.С., 82"),
            phone = null,
            menu = emptyList()
        ),
        Restaurant(
            id = Random.nextLong(),
            name = "Пхали-хинкали",
            address = Address("Большая Морская ул., 27"),
            phone = Phone("89219650524"),
            menu = emptyList()
        )
    )

    private val mockedPeople = listOf(
        Person(
            id = Random.nextLong(),
            name = "Ритуза",
            phone = null,
            emoji = Emoji("🐵")
        ),
        Person(
            id = Random.nextLong(),
            name = "Элина Зайникеева",
            phone = null,
            emoji = Emoji("🐰")
        ),
        Person(
            id = Random.nextLong(),
            name = "Павел Александров",
            phone = Phone("89041930639"),
            emoji = Emoji("🐙")
        ),
        Person(
            id = Random.nextLong(),
            name = "Жека Кауров",
            phone = null,
            emoji = Emoji("🐨")
        ),
        Person(
            id = Random.nextLong(),
            name = "Томочка Тараненко",
            phone = null,
            emoji = Emoji("🦄")
        ),
        Person(
            id = Random.nextLong(),
            name = "Тёма Шанин",
            phone = null,
            emoji = Emoji("🐼")
        ),
        Person(
            id = Random.nextLong(),
            name = "Макс Цекин",
            phone = null,
            emoji = Emoji("🐮")
        ),
        Person(
            id = Random.nextLong(),
            name = "Настя Станкова",
            phone = null,
            emoji = Emoji("🐱")
        )
    )

    private val mockedMeets = buildList {
        val currentTimeZone = TimeZone.currentSystemDefault()
        val instantTime = Clock.System.now()
        val todayDate = instantTime.toLocalDateTime(currentTimeZone)
        val weekAgoDate = instantTime.minus(7.days).toLocalDateTime(currentTimeZone)

        add(
            Meet(
                id = Random.nextLong(),
                type = Meet.Type.Active(todayDate),
                restaurant = mockedRestaurants.component1(),
                persons = mockedPeople.shuffled()
            )
        )

        add(
            Meet(
                id = Random.nextLong(),
                type = Meet.Type.Archived(todayDate),
                restaurant = mockedRestaurants.component2(),
                persons = mockedPeople.shuffled().take(3)
            )
        )

        add(
            Meet(
                id = Random.nextLong(),
                type = Meet.Type.Archived(weekAgoDate),
                restaurant = mockedRestaurants.component3(),
                persons = mockedPeople.shuffled().take(4)
            )
        )
    }.associateBy { it.id }

    private val stateFlow = MutableStateFlow(mockedMeets)

    private val meetsMutex = Mutex()

    override fun observeMeets(): Flow<List<Meet>> {
        return stateFlow
            .asStateFlow()
            .map { it.values.toList() }
    }

    override suspend fun createMeet(
        restaurant: Restaurant,
        persons: List<Person>,
        createDate: LocalDateTime
    ): MeetId {
        return meetsMutex.withLock {
            val id = Random.nextLong()
            stateFlow.value = stateFlow.value.toMutableMap()
                .apply {
                    val meet = Meet(id, Meet.Type.Active(createDate), restaurant, persons)
                    put(id, meet)
                }
            id
        }
    }
}