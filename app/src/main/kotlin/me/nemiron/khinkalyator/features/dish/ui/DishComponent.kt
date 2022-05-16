package me.nemiron.khinkalyator.features.dish.ui

import me.nemiron.khinkalyator.features.dish.domain.Dish

interface DishComponent {

    sealed interface Output {
        data class DishAdded(
            val dish: Dish
        ) : Output

        data class DishDeleted(
            val dish: Dish
        ) : Output

        object DishCreatingCompleted : Output
    }
}