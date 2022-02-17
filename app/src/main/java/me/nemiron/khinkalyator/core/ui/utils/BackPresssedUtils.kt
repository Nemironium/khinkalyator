package me.nemiron.khinkalyator.core.ui.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun dispatchOnBackPressed(context: Context) {
    val activity = context.getActivity() ?: return
    activity.onBackPressed()
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}