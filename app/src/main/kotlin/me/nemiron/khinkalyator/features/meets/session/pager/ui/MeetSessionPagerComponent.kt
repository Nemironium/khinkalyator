package me.nemiron.khinkalyator.features.meets.session.pager.ui

import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.core.widgets.OverflowMenuData
import me.nemiron.khinkalyator.features.dishes.domain.DishId
import me.nemiron.khinkalyator.features.people.domain.PersonId

interface MeetSessionPagerComponent {
    val initialPage: Page

    val title: LocalizedString

    val buttonTitle: LocalizedString

    val menuData: OverflowMenuData

    val peopleViewData: List<MeetSessionPersonViewData>

    val dishesViewData: List<MeetSessionDishViewData>

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