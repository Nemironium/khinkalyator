package me.nemiron.khinkalyator.features.people.person.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.aartikov.sesame.compose.form.control.InputControl
import me.aartikov.sesame.localizedstring.LocalizedString
import me.nemiron.khinkalyator.R
import me.nemiron.khinkalyator.core.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.core.theme.additionalColors
import me.nemiron.khinkalyator.core.utils.TextTransformations
import me.nemiron.khinkalyator.core.utils.resolve
import me.nemiron.khinkalyator.core.widgets.IconWithBackground
import me.nemiron.khinkalyator.core.widgets.KhContainedButton
import me.nemiron.khinkalyator.core.widgets.KhOutlinedTextField

@Composable
fun PersonUi(
    component: PersonComponent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 32.dp,
                bottom = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KhOutlinedTextField(
            inputControl = component.nameInputControl,
            placeholder = stringResource(R.string.person_name_placeholder),
            leadingIcon = {
                IconWithBackground(
                    painter = painterResource(R.drawable.ic_man_32),
                    backgroundColor = MaterialTheme.additionalColors.onSurfaceContainer
                )
            }
        )
        Spacer(Modifier.height(8.dp))
        KhOutlinedTextField(
            inputControl = component.phoneInputControl,
            placeholder = stringResource(R.string.person_phone_placeholder),
            leadingIcon = {
                IconWithBackground(
                    painter = painterResource(R.drawable.ic_phone_32),
                    backgroundColor = MaterialTheme.additionalColors.onSurfaceContainer
                )
            }
        )
        Spacer(Modifier.height(32.dp))
        KhContainedButton(
            modifier = Modifier
                .navigationBarsPadding(),
            text = component.buttonText.resolve(),
            onClick = component::onSubmitClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PersonPreview() {
    KhinkalyatorTheme {
        PersonUi(PreviewPersonComponent())
    }
}

class PreviewPersonComponent : PersonComponent {

    override val nameInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        textTransformation = TextTransformations.PersonName,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next
        )
    )

    override val phoneInputControl = InputControl(
        initialText = "",
        singleLine = true,
        maxLength = 30,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Phone
        ),
        textTransformation = TextTransformations.PhoneNumber
    )

    override val buttonText = LocalizedString.resource(R.string.person_add_button)
    override fun onSubmitClick() = Unit
}