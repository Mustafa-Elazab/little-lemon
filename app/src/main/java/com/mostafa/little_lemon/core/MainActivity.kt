package com.mostafa.little_lemon.core

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room

import com.mostafa.little_lemon.Constants
import com.mostafa.little_lemon.Constants.DATABASE_NAME
import com.mostafa.little_lemon.Constants.IS_REGISTERED_PREF
import com.mostafa.little_lemon.Constants.PREF_NAME
import com.mostafa.little_lemon.domain.MenuItemNetwork
import com.mostafa.little_lemon.domain.MenuNetwork
import com.mostafa.little_lemon.core.navigation.NavigationComposable
import com.mostafa.little_lemon.database.AppDatabase
import com.mostafa.little_lemon.ui.theme.LittleLemonTheme
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val sharedPreferences by lazy {
        getSharedPreferences(PREF_NAME, MODE_PRIVATE)
    }

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }

    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, DATABASE_NAME).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isRegistered = sharedPreferences.getBoolean(IS_REGISTERED_PREF, false)

        setContent {
            LittleLemonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavigationComposable(navController, isRegistered)
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            if (database.menuItemDao().isEmpty()) saveMenuToDatabase(fetchMenu())
        }
    }

    private suspend fun fetchMenu(): List<MenuItemNetwork> {
        val response = httpClient
            .get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
            .body<MenuNetwork>()
        return response.menu
    }

    private fun saveMenuToDatabase(menuItemsNetwork: List<MenuItemNetwork>) {
        val menuItemsRoom = menuItemsNetwork.map { it.toMenuItemRoom() }
        database.menuItemDao().insertAll(*menuItemsRoom.toTypedArray())
    }
}

@Composable
@Preview(name = Constants.LIGHT_MODE)
@Preview(name = Constants.DARK_MODE, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DefaultPreview() {
    LittleLemonTheme {
        val navController = rememberNavController()
        NavigationComposable(navController, false)
    }
}