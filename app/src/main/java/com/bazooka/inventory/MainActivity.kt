package com.bazooka.inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.bazooka.inventory.ui.navigation.BazookaNavigation
import com.bazooka.inventory.ui.theme.BAZOOKATheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BAZOOKATheme {
                val navController = rememberNavController()
                BazookaNavigation(navController = navController)
            }
        }
    }
}