package com.amar.modularnewsapp.ui.common

import androidx.compose.Composable
import androidx.compose.state
import androidx.ui.core.*
import androidx.ui.foundation.Clickable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.vector.DrawVector
import androidx.ui.input.KeyboardType
import androidx.ui.layout.*
import androidx.ui.material.surface.Surface
import androidx.ui.material.DrawerState
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ripple.Ripple
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.amar.modularnewsapp.R

private const val HINT = "Search News"
@Composable private val BODY1 =(MaterialTheme.typography()).body1
@Composable
fun TopAppBar(
    onDrawerStateChange: (DrawerState) -> Unit,
    backgroundColor: Color,
    onSearchClick: () -> Unit
) {
    val value = state { "" }
    Padding(6.dp) {
        Surface(
            color = backgroundColor,
            modifier = LayoutPadding(8.dp),
            shape = RoundedCornerShape(3.dp),
            elevation = 3.dp
        ) {
            Row() {
                // Load toggle image
                loadToggle(
                    onDrawerStateChange = onDrawerStateChange,
                    imageWidth = 30.dp,
                    imageHeight = 40.dp,
                    modifier = LayoutGravity.Center + LayoutPadding(left = 6.dp)
                )
                // text box
                customTextFeild(
                    hint = HINT,
                    value = value.value,
                    onValueChange = {value.value = it},
                    modifier = LayoutFlexible(1f) + LayoutGravity.Center + LayoutPadding(left = 8.dp, right = 8.dp)
                )
                // search bar
                customVectorImage(
                    onClick = onSearchClick,
                    imageHeight = 30.dp,
                    imageWidth = 20.dp,
                    modifier = LayoutGravity.Center + LayoutPadding(right = 6.dp)
                )
            }
        }
    }
}
@Composable
private fun loadToggle(
    onDrawerStateChange: (DrawerState) -> Unit,
    imageWidth: Dp?,
    imageHeight: Dp?,
    modifier: Modifier = Modifier.None,
    color: Color = Color.White
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
    editorStyle: TextStyle = BODY1,
    keyboardType: KeyboardType = KeyboardType.Ascii
) {
    Surface(modifier = modifier, color = (MaterialTheme.colors()).background) {
        Stack {
            Wrap(Alignment.TopLeft) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = editorStyle,
                    keyboardType = keyboardType
                )
            }
            if (value.isBlank()) {
                Wrap(Alignment.TopLeft) {
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
    onClick: () -> Unit,
    imageWidth: Dp?,
    imageHeight: Dp?,
    modifier: Modifier = Modifier.None,
    color: Color = Color.White
) {
    Ripple(bounded = false, color = Color.DarkGray) {
        Clickable(onClick = onClick) {
            Container(
                modifier = modifier,
                width = imageWidth,
                height = imageHeight
            ) {
                DrawVector(
                    vectorImage = vectorResource(R.drawable.ic_baseline_search_24),
                    tintColor = color,
                    alignment = Alignment.Center
                )
            }
        }
    }
}