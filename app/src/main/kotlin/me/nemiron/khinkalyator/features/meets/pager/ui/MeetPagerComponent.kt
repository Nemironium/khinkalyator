package me.nemiron.khinkalyator.features.meets.pager.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.core.widgets.OverflowMenuData
import me.nemiron.khinkalyator.common_domain.model.DishId
import me.nemiron.khinkalyator.common_domain.model.PersonId

interface MeetPagerComponent {
    val initialPage: Page

    val title: LocalizedString

    val buttonTitle: LocalizedString

    val menuData: OverflowMenuData

    val peopleViewData: List<MeetPersonViewData>

    val dishesViewData: List<MeetDishViewData>

    fun onCheckButtonClick()

    fun onDishPersonClick(dishId: DishId)

    fun onDishAddPersonClick(dishId: DishId)

    fun onPersonDishClick(personId: PersonId, selectedDishId: DishId)

    fun onPersonAddDishClick(personId: PersonId)

    enum class Page {
        People, Dishes
    }

    sealed interface Output {
        object CheckRequested : Output
        object DeleteRequested : Output
        class PersonDetailsRequested(val personId: PersonId, val selectedDishId: DishId?) : Output
        class DishDetailsRequested(val dishId: DishId) : Output
    }
}