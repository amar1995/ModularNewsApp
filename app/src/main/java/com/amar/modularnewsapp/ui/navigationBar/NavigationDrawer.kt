package com.amar.modularnewsapp.ui.navigationBar

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Image
import androidx.ui.foundation.ScrollableColumn
import androidx.ui.foundation.Text
import androidx.ui.graphics.BlendMode
import androidx.ui.graphics.Color
import androidx.ui.graphics.ColorFilter
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.res.imageResource
import androidx.ui.res.vectorResource
import androidx.ui.text.TextStyle
import androidx.ui.text.font.FontWeight
import androidx.ui.unit.dp
import androidx.ui.unit.sp
import com.amar.modularnewsapp.R
import com.amar.modularnewsapp.ui.Screen

private const val POWERED = "Powered By NewsApiOrg"

@Composable
fun NavigationDrawer(
    onDrawerStateChange: (DrawerState) -> Unit,
    backgroundColor: Color,
    context: Context
) {
    Column(modifier = Modifier.fillMaxHeight()) {
        SimpleImage(id = R.drawable.news_background)
        Surface(
            color = backgroundColor,
            modifier = Modifier.weight(1f).padding(8.dp)
        ) {
            ScrollableColumn() {
                Column {
                    DrawerButton(
                        icon = R.drawable.ic_baseline_headline_24,
                        label = Screen.TOP_HEADLINE.name,
                        isSelected = false,
                        action = {
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_general_24,
                        label = Screen.GENERAL.name,
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
                            color = (MaterialTheme.colors).onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_business_24,
                        label = Screen.BUSINESS.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_sport_24,
                        label = Screen.SPORTS.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_entertainment_24,
                        label = Screen.ENTERTAINMENT.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_health_24,
                        label = Screen.HEALTH.name,
                        isSelected = false,
                        action = {
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )

                    DrawerButton(
                        icon = R.drawable.ic_baseline_science_24,
                        label = Screen.SCIENCE.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    DrawerButton(
                        icon = R.drawable.ic_baseline_technology_24,
                        label = Screen.TECHNOLOGY.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                    Divider(color = Color.Gray)

                    DrawerButton(
                        icon = R.drawable.ic_baseline_settings_24,
                        label = Screen.SETTING.name,
                        isSelected = false,
                        action = {
                            makeToast(context)
                            onDrawerStateChange(DrawerState.Closed)
                        }
                    )
                }
            }
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Divider(color = (MaterialTheme.colors).onSurface)
            Text(
                text = POWERED,
                modifier = Modifier.gravity(align = Alignment.CenterHorizontally).padding(8.dp),
                style = (MaterialTheme.typography).body2.copy(color = (MaterialTheme.colors).onBackground)
            )
            Divider(color = (MaterialTheme.colors).onSurface)
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
        (MaterialTheme.colors).surface
    } else {
        ((MaterialTheme.colors).primary).copy(0.95f)
    }
    val backgroundColor = if (isSelected) {
        ((MaterialTheme.colors).primary).copy(alpha = 0.80f)
    } else {
        (MaterialTheme.colors).surface
    }
    Surface(
        modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp),
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        Button(
            onClick = action,
            backgroundColor = Color.Transparent,
            padding = InnerPadding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            ).copy(start = 8.dp, end = 8.dp),
            contentColor = MaterialTheme.colors.primary,
            elevation = 0.dp
        ) {
            Row(Modifier.fillMaxWidth()) {
                VectorImage(
                    id = icon,
                    tint = textIconColor
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = label,
                    style = ((MaterialTheme.typography).body2).copy(
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
    Image(
        asset = image,
        colorFilter = ColorFilter(tint, blendMode = BlendMode.srcIn),
        modifier = Modifier.preferredSize(20.dp)
    )
}

private fun makeToast(context: Context, msg: String = "TBD...") {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

@Composable
fun VectorImage(
    modifier: Modifier = Modifier, @DrawableRes id: Int,
    tint: Color = Color.Transparent
) {
    val vector = vectorResource(id)
    Image(
        asset = vector,
        colorFilter = ColorFilter(tint, blendMode = BlendMode.srcIn),
        modifier = modifier.preferredSize(20.dp)
    )
}