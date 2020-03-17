package com.amar.modularnewsapp.ui.common

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.Text
import androidx.ui.core.TextField
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.graphics.vector.VectorAsset
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.DrawerState
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ripple.Ripple
import androidx.ui.material.surface.Surface
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontFamily
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.amar.modularnewsapp.R

private const val HINT = "Search News"

@Composable
fun TopAppBar(
    onDrawerStateChange: (DrawerState) -> Unit,
    backgroundColor: Color,
    onSearchClick: () -> Unit
) {
    val value = state { "" }
        Surface(
            color = backgroundColor,
            modifier = LayoutPadding(14.dp),
            shape = RoundedCornerShape(3.dp),
            elevation = 1.dp
        ) {
            Row {
                // Load toggle image
                loadToggle(
                    onDrawerStateChange = onDrawerStateChange,
                    imageWidth = 30.dp,
                    imageHeight = 40.dp,
                    modifier = LayoutGravity.Center + LayoutPadding(start = 6.dp)
                )
                // text box
                customTextFeild(
                    hint = HINT,
                    value = value.value,
                    onValueChange = { value.value = it },
                    modifier = LayoutFlexible(1f) + LayoutGravity.Center + LayoutPadding(
                        start = 8.dp,
                        end = 8.dp
                    ),
                    color = backgroundColor
                )
                // search bar
                customVectorImage(
                    vectorImage = vectorResource(R.drawable.ic_baseline_search_24),
                    onClick = onSearchClick,
                    imageHeight = 30.dp,
                    imageWidth = 20.dp,
                    modifier = LayoutGravity.Center + LayoutPadding(end = 6.dp)
                )
            }
        }

}

@Composable
private fun loadToggle(
    onDrawerStateChange: (DrawerState) -> Unit,
    imageWidth: Dp?,
    imageHeight: Dp?,
    modifier: Modifier = Modifier.None,
    color: Color = (MaterialTheme.colors()).onBackground
) {
    Ripple(bounded = false, color = Color.DarkGray) {
        Clickable(onClick = { onDrawerStateChange(DrawerState.Opened) }) {
            Container(
                modifier = modifier,
                width = imageWidth,
                height = imageHeight
            ) {
                DrawVector(
                    vectorImage = vectorResource(R.drawable.ic_baseline_menu_24),
                    tintColor = color,
                    alignment = Alignment.Center
                )
            }
        }
    }
}

@Composable
private fun customTextFeild(
    hint: String,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier.None,
    editorStyle: TextStyle = (MaterialTheme.typography()).body1,
    keyboardType: KeyboardType = KeyboardType.Ascii,
    color: Color = (MaterialTheme.colors()).background
) {
    Surface(modifier = modifier, color = color) {
        Stack {
            Wrap(Alignment.TopStart) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = editorStyle,
                    keyboardType = keyboardType
                )
            }
            if (value.isBlank()) {
                Wrap(Alignment.TopStart) {
                    Text(
                        hint,
                        style = (MaterialTheme.typography()).body1.copy(color = Color.LightGray)
                    )
                }
            }
        }
    }
}

@Composable
private fun customVectorImage(
    vectorImage: VectorAsset,
    onClick: () -> Unit,
    imageWidth: Dp?,
    imageHeight: Dp?,
    modifier: Modifier = Modifier.None,
    color: Color = (MaterialTheme.colors()).onBackground
) {
    Ripple(bounded = false, color = Color.DarkGray) {
        Clickable(onClick = onClick) {
            Container(
                modifier = modifier,
                width = imageWidth,
                height = imageHeight
            ) {
                DrawVector(
                    vectorImage = vectorImage,
                    tintColor = color,
                    alignment = Alignment.Center
                )
            }
        }
    }
}