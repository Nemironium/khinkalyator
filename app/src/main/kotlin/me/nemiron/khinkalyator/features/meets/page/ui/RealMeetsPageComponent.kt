package me.nemiron.khinkalyator.features.meets.page.ui

import com.arkivanov.decompose.ComponentContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.nemiron.khinkalyator.features.emoji.domain.Emoji
import me.nemiron.khinkalyator.features.meets.domain.Meet
import me.nemiron.khinkalyator.features.meets.domain.MeetId
import me.nemiron.khinkalyator.features.people.domain.Person
import me.nemiron.khinkalyator.features.phone.domain.Phone
import me.nemiron.khinkalyator.features.restaraunts.domain.Address
import me.nemiron.khinkalyator.features.restaraunts.domain.Restaurant
import kotlin.random.Random

class RealMeetsPageComponent(
    componentContext: ComponentContext,
    private val onOutput: (MeetsPageComponent.Output) -> Unit
) : MeetsPageComponent, ComponentContext by componentContext {

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
        val todayDate = Clock.System.now().toLocalDateTime(currentTimeZone)

        add(
            Meet(
                id = Random.nextLong(),
                type = Meet.Type.Active,
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
                type = Meet.Type.Archived(todayDate),
                restaurant = mockedRestaurants.component3(),
                persons = mockedPeople.shuffled().take(4)
            )
        )
    }

    override val meetsViewData = mockedMeets.map(Meet::toMeetFullViewData)

    override fun onMeetAddClick() {
        onOutput(MeetsPageComponent.Output.NewMeetRequested)
    }

    override fun onMeetClick(meetId: MeetId) {
        onOutput(MeetsPageComponent.Output.MeetRequested(meetId))
    }
}