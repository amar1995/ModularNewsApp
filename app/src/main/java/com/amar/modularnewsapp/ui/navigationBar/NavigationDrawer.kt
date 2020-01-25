package com.amar.modularnewsapp.ui.navigationBar

import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.*
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.toArgb
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.material.surface.Surface
import androidx.ui.res.imageResource
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import com.amar.modularnewsapp.R

enum class NewsType {
    TopHeadline,
    Business,
    Entertainment,
    General,
    Health,
    Science,
    Sports,
    Technology
}
private const val POWERED = "Powered By NewsApiOrg"

@Composable
fun NavigationDrawer(
    onDrawerStateChange: (DrawerState) -> Unit,
    backgroundColor: Color
) {

        Column(ExpandedHeight) {
            Container(height = 150.dp, modifier = ExpandedWidth) {
                DrawImage(image = +imageResource(R.drawable.news_background))
            }
            Surface(
                color = backgroundColor,
                modifier = Flexible(1f) wraps Spacing(8.dp)
            ) {
                VerticalScroller() {
                    Column {
                        DrawerButton(
                            icon = R.drawable.ic_baseline_headline_24,
                            label = NewsType.TopHeadline.name,
                            isSelected = false,
                            action = {
                                onDrawerStateChange(DrawerState.Closed) }
                        )
                        DrawerButton(
                            icon = R.drawable.ic_baseline_general_24,
                            label = NewsType.General.name,
                            isSelected = false,
                            action = {
                                onDrawerStateChange(DrawerState.Closed) }
                        )
                        Divider(color = Color.Gray)
                        Text(
                            text = "Category",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500
                            ),
                            modifier = Spacing(8.dp)
                        )
                        DrawerButton(
                            icon = R.drawable.ic_baseline_business_24,
                            label = NewsType.Business.name,
                            isSelected = false,
                            action = {
                                onDrawerStateChange(DrawerState.Closed) }
                        )
                        DrawerButton(
                            icon = R.drawable.ic_baseline_sport_24,
                            label = NewsType.Sports.name,
                            isSelected = false,
                            action = {
                                onDrawerStateChange(DrawerState.Closed) }
                        )
                        DrawerButton(
                            icon = R.drawable.ic_baseline_entertainment_24,
                            label = NewsType.Entertainment.name,
                            isSelected = false,
                            action = {
                                onDrawerStateChange(DrawerState.Closed) }
                        )
                        DrawerButton(
                            icon = R.drawable.ic_baseline_health_24,
                            label = NewsType.Health.name,
                            isSelected = false,
                            action = {
                                onDrawerStateChange(DrawerState.Closed) }
                        )

                        DrawerButton(
                            icon = R.drawable.ic_baseline_science_24,
                            label = NewsType.Science.name,
                            isSelected = false,
                            action = {
                                onDrawerStateChange(DrawerState.Closed) }
                        )
                        DrawerButton(
                            icon = R.drawable.ic_baseline_technology_24,
                            label = NewsType.Technology.name,
                            isSelected = false,
                            action = {
                                onDrawerStateChange(DrawerState.Closed) }
                        )
                        Divider(color = Color.Gray)

                        DrawerButton(
                            icon = R.drawable.ic_baseline_settings_24,
                            label = "Setting",
                            isSelected = false,
                            action = {
                                onDrawerStateChange(DrawerState.Closed) }
                        )
                    }
                }
            }
            Padding(8.dp) {
                Column {
                    Divider(color = Color.Gray)
                    Text(
                        text = POWERED,
                        modifier = Gravity.Center wraps Spacing(8.dp)
                    )
                    Divider(color = Color.Gray)
                }
            }
        }

}

@Composable
private fun DrawerButton(
    @DrawableRes icon: Int,
    label: String,
    isSelected: Boolean,
    action: () -> Unit
) {
    val textIconColor = if (isSelected) {
        (+MaterialTheme.colors()).surface
    } else {
        ((+MaterialTheme.colors()).primary).copy(0.95f)
    }
    val backgroundColor = if (isSelected) {
        ((+MaterialTheme.colors()).primary).copy(alpha = 0.80f)
    } else {
        (+MaterialTheme.colors()).surface
    }
    println(backgroundColor)
    println(textIconColor)
    Surface(
        modifier = Modifier.None wraps Spacing(left = 8.dp, top = 8.dp, right = 8.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        Button(onClick = action, style = TextButtonStyle()) {
            Row(ExpandedWidth) {
                VectorImage(
                    id = icon,
                    tint = textIconColor
                )
                WidthSpacer(16.dp)
                Text(
                    text = label,
                    style = ((+MaterialTheme.typography()).body2).copy(
                        color = textIconColor
                    )
                )
            }
        }
    }
}

@Composable
private fun SimpleImage(@DrawableRes id: Int, tint: Color = Color.Transparent) {
    val image = +imageResource(id)
    WithDensity {
        Container(
            width = 20.dp,
            height = 20.dp
        ) {
            DrawImage(image, tint)
        }
    }
}

@Composable
private fun VectorImage(@DrawableRes id: Int, tint: Color = Color.Transparent) {
    val vector = +vectorResource(id)
    WithDensity {
        Container(
            width = vector.defaultWidth.toDp(),
            height = vector.defaultHeight.toDp()
        ) {
            DrawVector(vector, tint)
        }
    }
}