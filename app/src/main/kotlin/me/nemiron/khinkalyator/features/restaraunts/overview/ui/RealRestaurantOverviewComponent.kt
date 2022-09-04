package me.nemiron.khinkalyator.features.restaraunts.overview.ui

import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.launch
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.log
import me.nemiron.khinkalyator.core.utils.toComposeState
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.createMenuDetailsComponent
import me.nemiron.khinkalyator.features.restaraunts.createRestaurantDetailsComponent
import me.nemiron.khinkalyator.features.restaraunts.menu.domain.Dish
import me.nemiron.khinkalyator.features.restaraunts.menu.ui.MenuDetailsComponent
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.AddRestaurantUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.DeleteRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.GetRestaurantByIdUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.domain.UpdateRestaurantUseCase
import me.nemiron.khinkalyator.features.restaraunts.restaurant.ui.RestaurantDetailsComponent

class RealRestaurantOverviewComponent(
    componentContext: ComponentContext,
    private val configuration: RestaurantOverviewComponent.Configuration,
    private val onOutput: (RestaurantOverviewComponent.Output) -> Unit,
    private val componentFactory: ComponentFactory,
    private val getRestaurantById: GetRestaurantByIdUseCase,
    private val addRestaurant: AddRestaurantUseCase,
    private val updateRestaurant: UpdateRestaurantUseCase,
    private val deleteRestaurantById: DeleteRestaurantByIdUseCase
) : RestaurantOverviewComponent, ComponentContext by componentContext {

    private val commonState = State()

    private val navigation = StackNavigation<ChildConfiguration>()

    private val stack = childStack(
        source = navigation,
        initialConfiguration = ChildConfiguration.RestaurantDetails(configuration.toDetailsConfiguration()),
        handleBackButton = true,
        childFactory = ::createChild
    ).log("RestaurantOverview")

    private val coroutineScope = componentCoroutineScope()

    override val childStackState: ChildStack<*, RestaurantOverviewComponent.Child> by stack.toComposeState(
        lifecycle
    )

    init {
        lifecycle.doOnCreate(::loadRestaurant)
    }

    private fun createChild(
        config: ChildConfiguration,
        componentContext: ComponentContext
    ): RestaurantOverviewComponent.Child =
        when (config) {
            is ChildConfiguration.RestaurantDetails -> RestaurantOverviewComponent.Child.RestaurantDetails(
                componentFactory.createRestaurantDetailsComponent(
                    componentContext = componentContext,
                    configuration = config.restaurantConfiguration,
                    state = commonState,
                    onOutput = ::onRestaurantDetailsOutput
                )
            )
            is ChildConfiguration.MenuDetails -> RestaurantOverviewComponent.Child.MenuDetails(
                componentFactory.createMenuDetailsComponent(
                    componentContext = componentContext,
                    configuration = config.menuConfiguration,
                    state = commonState,
                    onOutput = ::onMenuDetailsOutput
                )
            )
        }

    private fun onRestaurantDetailsOutput(output: RestaurantDetailsComponent.Output) {
        return when (output) {
            is RestaurantDetailsComponent.Output.DishRequested -> {
                navigation.push(
                    ChildConfiguration.MenuDetails(
                        MenuDetailsComponent.Configuration.EditDish(output.dishId)
                    )
                )
            }
            is RestaurantDetailsComponent.Output.NewDishRequested -> {
                navigation.push(
                    ChildConfiguration.MenuDetails(
                        MenuDetailsComponent.Configuration.AddDish
                    )
                )
            }
            is RestaurantDetailsComponent.Output.RestaurantDeleteRequested -> {
                deleteRestaurant(configuration)
            }
            is RestaurantDetailsComponent.Output.RestaurantSaveRequested -> {
                createOrUpdateRestaurant(
                    configuration = configuration,
                    name = commonState.name,
                    address = commonState.address,
                    phone = commonState.phone,
                    dishes = commonState.dishes
                )
            }
        }
    }

    private fun onMenuDetailsOutput(output: MenuDetailsComponent.Output) = when (output) {
        MenuDetailsComponent.Output.MenuCloseRequested -> navigation.pop()
    }

    private fun loadRestaurant() {
        when (configuration) {
            is RestaurantOverviewComponent.Configuration.EditRestaurant -> {
                coroutineScope.launch {
                    val data = getRestaurantById(configuration.restaurantId)
                    data?.let { restaurant ->
                        commonState.setName(restaurant.name)
                        restaurant.phone?.let(commonState::setPhone)
                        restaurant.address?.let(commonState::setAddress)
                        commonState.setDishes(restaurant.menu)
                    }
                }
            }
            is RestaurantOverviewComponent.Configuration.NewRestaurant -> {
                // nothing
            }
        }
    }

    private fun deleteRestaurant(configuration: RestaurantOverviewComponent.Configuration) {
        when (configuration) {
            is RestaurantOverviewComponent.Configuration.NewRestaurant -> {
                // nothing
            }
            is RestaurantOverviewComponent.Configuration.EditRestaurant -> {
                coroutineScope.launch {
                    deleteRestaurantById(configuration.restaurantId)
                    onOutput(RestaurantOverviewComponent.Output.RestaurantCloseRequested)
                }
            }
        }
    }

    private fun createOrUpdateRestaurant(
        configuration: RestaurantOverviewComponent.Configuration,
        name: String?,
        address: Address?,
        phone: Phone?,
        dishes: List<Dish>
    ) {
        val restaurantName = name ?: return
        val restaurantAddress = address?.takeIf { it.value.isNotBlank() }
        val restaurantPhone = phone?.takeIf { it.value.isNotBlank() }

        coroutineScope.launch {
            when (configuration) {
                is RestaurantOverviewComponent.Configuration.EditRestaurant -> {
                    updateRestaurant(
                        restaurantId = configuration.restaurantId,
                        name = restaurantName,
                        phone = restaurantPhone,
                        address = restaurantAddress,
                        dishes = dishes
                    )
                }
                is RestaurantOverviewComponent.Configuration.NewRestaurant -> {
                    addRestaurant(
                        name = restaurantName,
                        address = restaurantAddress,
                        phone = restaurantPhone,
                        dishes = dishes
                    )
                }
            }
            onOutput(RestaurantOverviewComponent.Output.RestaurantCloseRequested)
        }
    }

    private sealed interface ChildConfiguration : Parcelable {
        @Parcelize
        class RestaurantDetails(
            val restaurantConfiguration: RestaurantDetailsComponent.Configuration
        ) : ChildConfiguration

        @Parcelize
        class MenuDetails(
            val menuConfiguration: MenuDetailsComponent.Configuration
        ) : ChildConfiguration
    }
}