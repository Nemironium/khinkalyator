package me.nemiron.khinkalyator.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.arkivanov.essenty.backpressed.BackPressedHandlerOwner
import me.aartikov.sesame.dialog.DialogControl

fun dispatchOnBackPressed(context: Context) {
    val activity = context.getActivity() ?: return
    activity.onBackPressed()
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun <T : Any, R : Any> DialogControl<T, R>.dismissOnBackPressed(owner: BackPressedHandlerOwner) {
    owner.backPressedHandler.register {
        if (isShowing) {
            dismiss()
            true
        } else false
    }
}

val <T : Any, R : Any> DialogControl<T, R>.isShowing: Boolean
    get() = stateFlow.value is DialogControl.State.Shown