package com.mostafa.little_lemon.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.mostafa.little_lemon.Constants
import com.mostafa.little_lemon.Constants.DATABASE_NAME
import com.mostafa.little_lemon.Constants.DEFAULT_CATEGORY
import com.mostafa.little_lemon.database.AppDatabase
import com.mostafa.little_lemon.database.MenuItemRoom
import com.mostafa.little_lemon.ui.theme.LittleLemonTheme

@Composable
fun Home(navController: NavHostController, onItemPressed: (MenuItemRoom) -> Unit) {
    val context = LocalContext.current
    val database by lazy {
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .build()
    }
    val databaseMenuItems by database.menuItemDao().getAll().observeAsState(emptyList())
    val orderMenuItems by remember { mutableStateOf(false) }
    var searchPhrase by remember { mutableStateOf("") }
    var categorySelected by remember { mutableStateOf("") }
    var menuItems =
        if (orderMenuItems) databaseMenuItems.sortedBy { it.title } else databaseMenuItems

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            HomeHeader(navController)
            HeroSection(onPhraseSelected = { phrase ->
                searchPhrase = phrase
            })
            MenuCategories(onItemClick = { category ->
                categorySelected = category
            })
        }
        if (searchPhrase.isNotEmpty()) {
            menuItems = databaseMenuItems.filter {
                it.title.contains(searchPhrase, ignoreCase = true)
            }
        }
        if (categorySelected.isNotEmpty()) {
            menuItems = if (categorySelected == DEFAULT_CATEGORY) {
                databaseMenuItems.sortedBy { it.id }
            } else {
                databaseMenuItems.filter {
                    it.category.contains(categorySelected, ignoreCase = true)
                }
            }
        }
        this@LazyColumn.menuItems(menuItems = menuItems, onItemPressed = onItemPressed)
    }
}

@Composable
@Preview(name = Constants.LIGHT_MODE)
@Preview(name = Constants.DARK_MODE, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun HomePreview() {
    LittleLemonTheme {
        Surface {
            val navController = rememberNavController()
            Home(navController, onItemPressed = {})
        }
    }
}