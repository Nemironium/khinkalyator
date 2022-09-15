package me.nemiron.khinkalyator.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcherOwner
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackHandler
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.arkivanov.essenty.backhandler.backHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.aartikov.sesame.dialog.DialogControl

fun dispatchOnBackPressed(context: Context) {
    val activity = context.getActivity() as? ComponentActivity ?: return
    activity.onBackPressedDispatcher.onBackPressed()
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

// TODO: check that works is fine
fun <T : Any, R : Any> DialogControl<T, R>.dismissOnBackPressed(
    owner: BackHandlerOwner,
    coroutineScope: CoroutineScope
) {
    val backCallback = BackCallback(isEnabled = false) {
        dismiss()
    }

    stateFlow.onEach { state ->
        val isShowing = state is DialogControl.State.Shown
        backCallback.isEnabled = isShowing
    }.launchIn(coroutineScope)
    owner.backHandler.register(backCallback)
}