package me.nemiron.khinkalyator

import me.nemiron.khinkalyator.core.coreModule
import me.nemiron.khinkalyator.features.dishes.dishesModule
import me.nemiron.khinkalyator.features.meets.meetsModule
import me.nemiron.khinkalyator.features.people.peopleModule
import me.nemiron.khinkalyator.features.restaraunts.restaurantsModule

val allModules = listOf(
    coreModule,
    peopleModule,
    restaurantsModule,
    dishesModule,
    meetsModule
)