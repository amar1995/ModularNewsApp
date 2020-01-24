package com.amar.modularnewsapp.ui.navigationBar

import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.core.WithDensity
import androidx.ui.core.dp
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.toArgb
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.layout.*
import androidx.ui.material.Button
import androidx.ui.material.DrawerState
import androidx.ui.material.MaterialTheme
import androidx.ui.material.TextButtonStyle
import androidx.ui.material.surface.Surface
import androidx.ui.res.imageResource
import androidx.ui.res.vectorResource
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

@Composable
fun NavigationDrawer(
    onDrawerStateChange: (DrawerState) -> Unit,
    backgroundColor: Color
) {
    Padding(8.dp) {
        Column(ExpandedHeight) {
            Surface(
                color = backgroundColor,
                modifier = Spacing(8.dp)
            ) {
                DrawerButton(icon = R.drawable.ic_baseline_business_24, label = "Business", isSelected = false, action = {
                    onDrawerStateChange(DrawerState.Closed)
                })
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