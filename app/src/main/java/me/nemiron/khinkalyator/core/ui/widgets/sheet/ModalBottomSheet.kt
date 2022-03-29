package me.nemiron.khinkalyator.core.ui.widgets.sheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.aartikov.sesame.dialog.DialogControl
import me.nemiron.khinkalyator.core.ui.theme.appShapes

/*
* ModalBottomSheetDialogLayout требуется для отображения BottomSheet на экране.
* Так же данная функция берет на себя ответственность за исправление бага
* во время анимации закрытия клавиатуры. По этой причине здесь имеется различные
* проверки на видимость modalBottomSheetState.
* Поэтому в случае открытой клавиатуры и закрытого modalBottomSheetState необходимо:
* цвет фона сделать прозрачным, установить Elevation в 0, скрыть контент.
* WARNING: sheetContent не может быть пустым, по этой причине используется Spacer
* */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T : Any, F : Any> ModalBottomSheet(
    dialogControl: DialogControl<T, F>,
    modifier: Modifier = Modifier,
    sheetContent: @Composable (T) -> Unit
) {
    val state by dialogControl.stateFlow.collectAsState()
    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )

    if (state is DialogControl.State.Shown) {
        if (modalBottomSheetState.currentValue != ModalBottomSheetValue.Expanded) {
            LaunchedEffect(modalBottomSheetState) {
                modalBottomSheetState.show()
            }
        }
    } else {
        if (modalBottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
            LaunchedEffect(modalBottomSheetState) {
                modalBottomSheetState.hide()
            }
        }
    }

    if (modalBottomSheetState.currentValue != ModalBottomSheetValue.Hidden) {
        DisposableEffect(Unit) {
            onDispose {
                dialogControl.dismiss()
            }
        }
    }

    val sheetElevation = if (modalBottomSheetState.isVisible) {
        ModalBottomSheetDefaults.Elevation
    } else {
        0.dp
    }

    val sheetBackgroundColor = if (modalBottomSheetState.isVisible) {
        MaterialTheme.colors.background
    } else {
        Color.Transparent
    }

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = modalBottomSheetState,
        sheetShape = MaterialTheme.appShapes.sheet.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        sheetElevation = sheetElevation,
        sheetBackgroundColor = sheetBackgroundColor,
        sheetContent = {
            val data = dialogControl.dataOrNull
            if (data != null && modalBottomSheetState.isVisible) {
                sheetContent(data)
            } else {
                Spacer(Modifier.height(1.dp))
            }
        },
        content = {}
    )
}

/**
 * ModalBottomSheetDialogLayout для отображения BottomSheet без [DialogControl] на основе
 * изменяемого состояния [data].
 * Когда [data] не равна null, то будет показан [sheetContent] для текущей [data].
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T>ModalBottomSheet(
    data: T?,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { },
    sheetContent: @Composable (data: T) -> Unit
) {
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { newState ->
            if (newState == ModalBottomSheetValue.Hidden) {
                onDismiss()
            }
            true
        }
    )

    LaunchedEffect(data) {
        if (data == null) {
            modalSheetState.hide()
        } else {
            modalSheetState.show()
        }
    }

    val sheetElevation = if (modalSheetState.isVisible) {
        ModalBottomSheetDefaults.Elevation
    } else {
        0.dp
    }

    val sheetBackgroundColor = if (modalSheetState.isVisible) {
        MaterialTheme.colors.background
    } else {
        Color.Transparent
    }

    ModalBottomSheetLayout(
        modifier = modifier,
        sheetState = modalSheetState,
        sheetShape = MaterialTheme.appShapes.sheet.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        sheetElevation = sheetElevation,
        sheetBackgroundColor = sheetBackgroundColor,
        sheetContent = {
            if (modalSheetState.isVisible && data != null) {
                sheetContent(data)
            } else {
                Spacer(Modifier.height(1.dp))
            }
        },
        content = {}
    )
}

val <T : Any, R : Any> DialogControl<T, R>.dataOrNull: T?
    get() = (this.stateFlow.value as? DialogControl.State.Shown)?.data