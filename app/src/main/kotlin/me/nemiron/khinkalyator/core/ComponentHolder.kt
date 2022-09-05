package me.nemiron.khinkalyator.core

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.nemiron.khinkalyator.core.utils.componentCoroutineScope
import me.nemiron.khinkalyator.core.utils.currentConfiguration
import me.nemiron.khinkalyator.core.utils.currentInstance
import me.nemiron.khinkalyator.core.utils.toComposeState

/**
 * Allows to dynamically create Component [T] and change it state with [configuration]
 */
class ComponentHolder<C : Parcelable, T : Any>(
    componentContext: ComponentContext,
    key: String,
    removeOnBackPressed: Boolean,
    private val componentFactory: (configuration: C, ComponentContext) -> T
) : ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration<C>>()

    private val coroutineScope = componentCoroutineScope()

    private val stack = childStack(
        source = navigation,
        initialConfiguration = Configuration.None,
        key = "ComponentHolder: $key",
        handleBackButton = removeOnBackPressed,
        childFactory = ::createComponent
    )

    private val childStackState by stack.toComposeState(lifecycle)

    private val configurationState by derivedStateOf {
        childStackState.currentConfiguration
    }

    private val isConfigurationSet by derivedStateOf {
        when (configurationState) {
            is Configuration.Component -> true
            is Configuration.None -> false
        }
    }

    private val backCallback = BackCallback(isEnabled = false) {
        configuration = null
        updateBackCallback(false)
    }

    val component by derivedStateOf {
        childStackState.currentInstance.component
    }

    var configuration: C?
        get() {
            return when (val routerConfiguration = configurationState) {
                is Configuration.Component -> routerConfiguration.componentConfiguration
                is Configuration.None -> null
            }
        }
        set(value) {
            if (value != null) {
                navigation.replaceCurrent(Configuration.Component(value))
            } else {
                navigation.replaceCurrent(Configuration.None)
            }
        }

    init {
        backHandler.register(backCallback)
        updateBackCallback(removeOnBackPressed)

        snapshotFlow { isConfigurationSet }
            .map { it && removeOnBackPressed }
            .onEach { updateBackCallback(it) }
            .launchIn(coroutineScope)
    }

    private fun updateBackCallback(isEnabled: Boolean) {
        backCallback.isEnabled = isEnabled
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

    sealed interface Configuration<out C : Parcelable> : Parcelable {
        @Parcelize
        object None : Configuration<Nothing>

        @Parcelize
        data class Component<C : Parcelable>(val componentConfiguration: C) : Configuration<C>
    }

    class Child<T>(val component: T?)
}

inline fun <reified C : Parcelable, reified T : Any> ComponentContext.componentHolder(
    key: String = T::class.java.name,
    removeOnBackPressed: Boolean = false,
    noinline componentFactory: (configuration: C, ComponentContext) -> T
): ComponentHolder<C, T> {
    return ComponentHolder(
        componentContext = this,
        key = key,
        removeOnBackPressed = removeOnBackPressed,
        componentFactory = componentFactory
    )
}