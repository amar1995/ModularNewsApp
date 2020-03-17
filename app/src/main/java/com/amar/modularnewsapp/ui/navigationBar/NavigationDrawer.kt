package com.amar.modularnewsapp.ui.navigationBar

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.foundation.SimpleImage
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.Divider
import androidx.ui.material.DrawerState
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.res.imageResource
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.dp
import androidx.ui.unit.sp
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
    backgroundColor: Color,
    context: Context
) {
    Column(LayoutHeight.Fill) {
        Container(height = 150.dp, modifier = LayoutWidth.Fill) {
            SimpleImage(image = imageResource(R.drawable.news_background))
        }
        Surface(
            color = backgroundColor,
            modifier = LayoutFlexible(1f) + LayoutPadding(8.dp)
        ) {
            VerticalScroller() {
                Column {
                    DrawerButton(
                        icon = R.drawable.ic_baseline_headline_24,
                        label = NewsType.TopHeadline.name,
                        isSelected = false,
                        action = {
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_general_24,
                        label = NewsType.General.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    Divider(color = Color.Gray)
                    Text(
                        text = "Category",
                        style = TextStyle(
                            color = (MaterialTheme.colors()).onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500
                        ),
                        modifier = LayoutPadding(8.dp)
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_business_24,
                        label = NewsType.Business.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_sport_24,
                        label = NewsType.Sports.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_entertainment_24,
                        label = NewsType.Entertainment.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_health_24,
                        label = NewsType.Health.name,
                        isSelected = false,
                        action = {
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )

                    DrawerButton(
                        icon = R.drawable.ic_baseline_science_24,
                        label = NewsType.Science.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_technology_24,
                        label = NewsType.Technology.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    Divider(color = Color.Gray)

                    DrawerButton(
                        icon = R.drawable.ic_baseline_settings_24,
                        label = "Setting",
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                }
            }
        }
        Column(LayoutPadding(8.dp)) {
            Divider(color = (MaterialTheme.colors()).onSurface)
            Text(
                text = POWERED,
                modifier = LayoutGravity.Center + LayoutPadding(8.dp),
                style = (MaterialTheme.typography()).body2.copy(color = (MaterialTheme.colors()).onBackground)
            )
            Divider(color = (MaterialTheme.colors()).onSurface)
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
        (MaterialTheme.colors()).surface
    } else {
        ((MaterialTheme.colors()).primary).copy(0.95f)
    }
    val backgroundColor = if (isSelected) {
        ((MaterialTheme.colors()).primary).copy(alpha = 0.80f)
    } else {
        (MaterialTheme.colors()).surface
    }
    Surface(
        modifier = LayoutPadding(start = 8.dp, top = 8.dp, end = 8.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        Button(onClick = action,
            backgroundColor = Color.Transparent,
            shape = MaterialTheme.shapes().button,
            paddings = EdgeInsets(
                left = 16.dp,
                right = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            ).copy(left = 8.dp, right = 8.dp),
            contentColor = MaterialTheme.colors().primary,
            elevation = 0.dp){
            Row(LayoutWidth.Fill) {
                VectorImage(
                    id = icon,
                    tint = textIconColor
                )
                Spacer(LayoutWidth(16.dp))
                Text(
                    text = label,
                    style = ((MaterialTheme.typography()).body2).copy(
                        color = textIconColor
                    )
                )
            }
        }
    }
}

@Composable
private fun SimpleImage(@DrawableRes id: Int, tint: Color = Color.Transparent) {
    val image = imageResource(id)
    Container(
        width = 20.dp,
        height = 20.dp
    ) {
        SimpleImage(image, tint)
    }

}
private fun makeToast(context: Context, msg: String = "TBD...") {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}
// --- dev03
//@Composable
//private fun VectorImage(@DrawableRes id: Int, tint: Color = Color.Transparent) {
//    val vector = vectorResource(id)
//    WithDensity {
//        Container(
//            width = vector.defaultWidth.toDp(),
//            height = vector.defaultHeight.toDp()
//        ) {
//            DrawVector(vector, tint)
//        }
//    }
//}

@Composable
fun VectorImage(
    modifier: Modifier = Modifier.None, @DrawableRes id: Int,
    tint: Color = Color.Transparent
) {
    val vector = vectorResource(id)
    Container(
        modifier = modifier + LayoutSize(vector.defaultWidth, vector.defaultHeight)
    ) {
        DrawVector(vector, tint)
    }

}