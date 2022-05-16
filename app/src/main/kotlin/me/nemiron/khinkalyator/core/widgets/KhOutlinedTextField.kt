package me.nemiron.khinkalyator.core.widgets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.aartikov.sesame.compose.form.control.InputControl
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.appShapes
import me.nemiron.khinkalyator.core.theme.appTypography
import me.nemiron.khinkalyator.core.utils.resolve

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KhOutlinedTextField(
    inputControl: InputControl,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    textFieldHeight: Dp = Dp.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = inputControl.visualTransformation
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.bringIntoViewRequester(bringIntoViewRequester)
    ) {
        val focusRequester = remember { FocusRequester() }

        SideEffect {
            if (inputControl.hasFocus) {
                focusRequester.requestFocus()
            }
        }

        LaunchedEffect(inputControl) {
            inputControl.scrollToItEvent.collectLatest {
                bringIntoViewRequester.bringIntoView()
            }
        }

        val textSelectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colors.secondary,
            backgroundColor = MaterialTheme.colors.secondary.copy(alpha = 0.4f)
        )
        CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(textFieldHeight)
                    .focusRequester(focusRequester)
                    .onFocusEvent { focusState ->
                        if (focusState.isFocused) {
                            coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                        }
                    }
                    .onFocusChanged { inputControl.onFocusChanged(it.isFocused) },
                value = inputControl.text,
                onValueChange = inputControl::onTextChanged,
                isError = inputControl.error != null,
                visualTransformation = visualTransformation,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.appTypography.text1
                    )
                },
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                textStyle = MaterialTheme.appTypography.text1,
                shape = MaterialTheme.appShapes.textField,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colors.onSurface,
                    cursorColor = MaterialTheme.colors.secondary,
                    backgroundColor = MaterialTheme.colors.surface,
                    placeholderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    focusedBorderColor = MaterialTheme.colors.secondary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
                ),
                singleLine = inputControl.singleLine,
                maxLines = maxLines,
                keyboardOptions = inputControl.keyboardOptions,
                enabled = inputControl.enabled
            )
            ErrorText(inputControl.error?.resolve())
        }
    }
}

@Composable
private fun ErrorText(
    errorText: String?,
    modifier: Modifier = Modifier
) {
    errorText?.let {
        Text(
            modifier = modifier.padding(top = 8.dp),
            text = errorText,
            style = MaterialTheme.appTypography.text2,
            color = MaterialTheme.colors.error
        )
    }
}

@Preview(showBackground = true)
@Composable
fun KhOutlinedTextFieldPreview() {
    val inputControl = InputControl(
        initialText = "initialText",
        singleLine = true,
        maxLength = 16,
        keyboardOptions = KeyboardOptions.Default
    )
    KhinkalyatorTheme {
        KhOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            inputControl = inputControl,
            placeholder = "Placeholder",
        )
    }
}