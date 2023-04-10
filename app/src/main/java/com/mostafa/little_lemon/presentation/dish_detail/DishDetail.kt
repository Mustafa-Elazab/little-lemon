package com.mostafa.little_lemon.presentation.dish_detail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.mostafa.little_lemon.Constants
import com.mostafa.little_lemon.database.AppDatabase
import com.mostafa.little_lemon.database.MenuItemRoom
import com.mostafa.little_lemon.ui.theme.LittleLemonTheme

@Composable
fun DishDetail(
    onBackButtonClicked: () -> Unit,
    id: String?,
) {
    val context = LocalContext.current
    val database by lazy {
        Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, Constants.DATABASE_NAME
        ).build()
    }
    val item = database.menuItemDao().getItem(id ?: "1").observeAsState(MenuItemRoom())
    val size = LocalConfiguration.current
    val headerHeight = size.screenHeightDp * 0.5
    val contentHeight = size.screenHeightDp * 0.53

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        DetailImageHeader(
            menuItem = item.value,
            onBackButtonClicked = onBackButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight.dp)
        )
        DetailCardContent(
            modifier = Modifier
                .fillMaxWidth()
                .height(contentHeight.dp),
            menuItem = item.value
        )
    }
}

@Composable
@Preview(name = Constants.LIGHT_MODE)
@Preview(name = Constants.DARK_MODE, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun ProfilePreview() {
    LittleLemonTheme {
        Surface {
            DishDetail(onBackButtonClicked = {}, "1")
        }
    }
}