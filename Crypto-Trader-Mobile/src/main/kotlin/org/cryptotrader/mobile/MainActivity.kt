package org.cryptotrader.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemUi = rememberSystemUiController()
            systemUi.setSystemBarsColor(color = MaterialTheme.colorScheme.background, darkIcons = true)

            val navController = rememberNavController()
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavHost(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, vm: MainViewModel = hiltViewModel()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(vm)
        }
    }
}

@Composable
fun HomeScreen(vm: MainViewModel) {
    // Example Coil usage
    AsyncImage(
        model = "https://dummyimage.com/600x200/1c1c1c/ffffff&text=Crypto+Trader",
        contentDescription = "Hero"
    )
    // Example text from Flow state
    Text(text = vm.greeting)
}