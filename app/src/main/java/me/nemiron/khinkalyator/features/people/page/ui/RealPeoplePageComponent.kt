package me.nemiron.khinkalyator.features.people.page.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import me.nemiron.khinkalyator.features.people.domain.Emoji
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.people.domain.PersonId
import me.nemiron.khinkalyator.features.phone.domain.Phone
import kotlin.random.Random

class RealPeoplePageComponent(
    componentContext: ComponentContext
) : PeoplePageComponent, ComponentContext by componentContext {

    private var mockedPeople by mutableStateOf(
        listOf(
            Person(
                id = Random.nextLong(),
                name = "Ритуза",
                phone = null,
                emoji = Emoji("🦜")
            ),
            Person(
                id = Random.nextLong(),
                name = "Элина Зайникеева",
                phone = null,
                emoji = Emoji("🐓")
            ),
            Person(
                id = Random.nextLong(),
                name = "Павел Александров",
                phone = Phone("89041930639"),
                emoji = Emoji("🐺")
            ),
            Person(
                id = Random.nextLong(),
                name = "Жека Кауров",
                phone = null,
                emoji = Emoji("🦬")
            ),
            Person(
                id = Random.nextLong(),
                name = "Томочка Тараненко",
                phone = null,
                emoji = Emoji("🐎")
            ),
            Person(
                id = Random.nextLong(),
                name = "Тёма Шанин",
                phone = null,
                emoji = Emoji("🐩")
            ),
            Person(
                id = Random.nextLong(),
                name = "Макс Цекин",
                phone = null,
                emoji = Emoji("🐫")
            ),
            Person(
                id = Random.nextLong(),
                name = "Настя Станкова",
                phone = null,
                emoji = Emoji("🐬")
            )
        )
    )

    override val peopleViewData by derivedStateOf {
        mockedPeople.map(Person::toPersonFullViewData)
    }

    override fun onPersonAddClick() {
        // TODO: call DialogControl
    }

    override fun onPersonDeleteClick(personId: PersonId) {
        // TODO: call DB for delete item
        mockedPeople = mockedPeople.filter { it.id == personId }
    }

    override fun onPersonClick(personId: PersonId) {
        // TODO: call DialogControl
    }
}