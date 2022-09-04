package me.nemiron.khinkalyator.core.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.Child
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import timber.log.Timber

/**
 * Creates [ChildStack] for preview.
 */
fun <T : Any> createFakeChildStack(instance: T): ChildStack<*, T> {
    return ChildStack(
        active = Child.Created(
            configuration = "<fake>",
            instance = instance
        )
    )
}

/**
 * Logs [ChildStack] new entries when updates are received.
 */
fun <C : Any, T : Any> Value<ChildStack<C, T>>.log(routerName: String): Value<ChildStack<C, T>> {
    subscribe {
        val childName = it.active.instance::class.qualifiedName
        val message = "$routerName router navigate to $childName"
        Timber.tag("Navigation").d(message)
    }
    return this
}

/**
 * Returns Child [T] from top of back-stack.
 *
 * Never is null, because in Decompose back-stack cannot be empty.
 */
val <C : Any, T : Any> ChildStack<C, T>.currentInstance get() = this.active.instance
val <C : Any, T : Any> Value<ChildStack<C, T>>.currentInstance get() = value.currentInstance

/**
 * Returns Configuration [C] from top of back-stack.
 *
 * Never is null, because in Decompose back-stack cannot be empty.
 */
val <C : Any, T : Any> ChildStack<C, T>.currentConfiguration get() = active.configuration
val <C : Any, T : Any> Value<ChildStack<C, T>>.currentConfiguration get() = value.currentConfiguration

/**
 * Replaces all current back-stack with [configuration].
 */
fun <C : Any> StackNavigation<C>.replaceAll(configuration: C) {
    navigate(
        transformer = { listOf(configuration) },
        onComplete = { _, _ -> }
    )
}

/**
 * Creates [CoroutineScope], bound to the Component [Lifecycle].
 */
fun LifecycleOwner.componentCoroutineScope(): CoroutineScope {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    } else {
        scope.cancel()
    }

    return scope
}

/**
 * Transforms Decompose [Value]<[T]> to Compose [State]<[T]>, bound to the [Lifecycle]
 */
fun <T : Any> Value<T>.toComposeState(lifecycle: Lifecycle): State<T> {
    val state: MutableState<T> = mutableStateOf(this.value)

    if (lifecycle.state != Lifecycle.State.DESTROYED) {
        val observer = { value: T -> state.value = value }
        subscribe(observer)
        lifecycle.doOnDestroy {
            unsubscribe(observer)
        }
    }

    return state
}