package me.nemiron.khinkalyator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.root.ui.RealRootComponent
import me.nemiron.khinkalyator.root.ui.RootUi

/**
 * В манифесте у MainActivity перечислены все возможные android:configChanges. Благодаря этому
 * активити переживает смену конфигурации, а обновление UI-я происходит за счет рекомпозиции в Jetpack Compose.
 */
class KhinkalyatorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = RealRootComponent(defaultComponentContext())

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            KhinkalyatorTheme {
                RootUi(root)
            }
        }
    }
}