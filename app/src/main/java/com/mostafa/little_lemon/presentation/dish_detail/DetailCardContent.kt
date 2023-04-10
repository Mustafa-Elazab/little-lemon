package com.mostafa.little_lemon.presentation.dish_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mostafa.little_lemon.Constants
import com.mostafa.little_lemon.components.AddOrRemoveItemComponent
import com.mostafa.little_lemon.database.MenuItemRoom
import com.mostafa.little_lemon.ui.theme.Peach
import com.mostafa.little_lemon.ui.theme.PrimaryGray
import com.mostafa.little_lemon.ui.theme.White
import com.mostafa.little_lemon.ui.theme.Yellow
import com.mostafa.little_lemon.R
import java.util.*

@Composable
fun DetailCardContent(
    modifier: Modifier = Modifier,
    menuItem: MenuItemRoom
) {
    var total by remember { mutableStateOf(menuItem.price) }
    total = menuItem.price
    Card(
        shape = RoundedCornerShape(
            topEnd = 30.dp,
            topStart = 30.dp
        ),
        elevation = 0.dp,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.padding(10.dp)
        ) {
            item {
                Column(modifier = Modifier.fillParentMaxHeight()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = menuItem.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                        Card(
                            modifier = Modifier.clickable { },
                            shape = RoundedCornerShape(26.dp),
                            backgroundColor = Peach
                        ) {
                            Text(
                                text = menuItem.category.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.US
                                    ) else it.toString()
                                },
                                textAlign = TextAlign.Center,
                                color = White,
                                modifier = Modifier.padding(8.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "${stringResource(id = R.string.price_label)} $%.2f".format(menuItem.price),
                        color = PrimaryGray,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp)
                    )
                    Text(
                        text = menuItem.description,
                        color = PrimaryGray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 8.dp)
                    )
                    AddOrRemoveItemComponent(onButtonsPressed = { quantity ->
                        total = menuItem.price * quantity
                    })
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = {

                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Yellow,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(16)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                modifier = Modifier
                                    .size(26.dp)
                                    .padding(end = 8.dp),
                                contentDescription = Constants.BUTTON_ICON,
                                tint = Color.Unspecified
                            )
                            Text(
                                text =
                                "${stringResource(id = R.string.add_to_cart_label)} $%.2f".format(
                                    total
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}