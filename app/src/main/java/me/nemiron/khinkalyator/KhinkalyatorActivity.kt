package me.nemiron.khinkalyator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.arkivanov.decompose.defaultComponentContext
import me.nemiron.khinkalyator.core.ui.theme.KhinkalyatorTheme
import me.nemiron.khinkalyator.features.people.data.InMemoryPeopleStorage
import me.nemiron.khinkalyator.features.restaraunts.data.InMemoryRestaurantsStorage
import me.nemiron.khinkalyator.root.ui.RealRootComponent
import me.nemiron.khinkalyator.root.ui.RootUi

/**
 * All activity android:configChanges listed in AndroidManifest.xml.
 * All UI updates occur through Compose recomposition instead activity recreating
 */
class KhinkalyatorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        installSplashScreen()

        val peopleStorage = InMemoryPeopleStorage()
        val restaurantStorage = InMemoryRestaurantsStorage()
        val root = RealRootComponent(defaultComponentContext(), peopleStorage, restaurantStorage)

        setContent {
            KhinkalyatorTheme {
                RootUi(root)
            }
        }
    }
}