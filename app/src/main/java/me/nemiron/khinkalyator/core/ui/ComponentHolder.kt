package me.nemiron.khinkalyator.core.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.replaceCurrent
import com.arkivanov.decompose.router.router
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize
import me.nemiron.khinkalyator.core.ui.utils.currentConfiguration
import me.nemiron.khinkalyator.core.ui.utils.toComposeState

/**
 * Абстракция, позволяющая динамически создавать Component [T] и менять его состояние через
 * Configuration [C]
 */
class ComponentHolder<C : Parcelable, T : Any>(
    componentContext: ComponentContext,
    key: String,
    removeOnBackPressed: Boolean,
    private val componentFactory: (configuration: C, ComponentContext) -> T
) : ComponentContext by componentContext {

    private val router = router<Configuration<C>, Child<T>>(
        initialConfiguration = Configuration.None,
        key = "ComponentHolder: $key",
        handleBackButton = removeOnBackPressed,
        childFactory = ::createComponent
    )

    private val routerState by router.state.toComposeState(lifecycle)

    init {
        if (removeOnBackPressed) {
            removeOnBackPressed()
        }
    }

    val component by derivedStateOf {
        routerState.activeChild.instance.component
    }

    var configuration: C?
        get() {
            router.currentConfiguration
            return when (val routerConfiguration = router.currentConfiguration) {
                is Configuration.Component -> routerConfiguration.componentConfiguration
                is Configuration.None -> null
            }
        }
        set(value) {
            if (value != null) {
                router.replaceCurrent(Configuration.Component(value))
            } else {
                router.replaceCurrent(Configuration.None)
            }
        }

    private fun createComponent(
        config: Configuration<C>,
        componentContext: ComponentContext
    ): Child<T> {
        return when (config) {
            is Configuration.Component -> {
                Child(componentFactory(config.componentConfiguration, componentContext))
            }
            Configuration.None -> {
                Child(null)
            }
        }
    }

    sealed class Configuration<out C : Parcelable> : Parcelable {
        @Parcelize
        object None : Configuration<Nothing>()

        @Parcelize
        data class Component<C : Parcelable>(val componentConfiguration: C) : Configuration<C>()
    }

    class Child<T>(val component: T?)
}

inline fun <reified C : Parcelable, reified T : Any> ComponentContext.componentHolder(
    key: String = T::class.java.name,
    removeOnBackPressed: Boolean = false,
    noinline componentFactory: (configuration: C, ComponentContext) -> T
): ComponentHolder<C, T> {
    return ComponentHolder<C, T>(
        componentContext = this,
        key = key,
        removeOnBackPressed = removeOnBackPressed,
        componentFactory = componentFactory
    )
}

fun <C : Parcelable, T : Any> ComponentHolder<C, T>.removeOnBackPressed() {
    backPressedHandler.register {
        if (configuration == null) {
            false
        } else {
            configuration = null
            true
        }
    }
}