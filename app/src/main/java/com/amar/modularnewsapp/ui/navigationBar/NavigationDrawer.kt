package com.amar.modularnewsapp.ui.navigationBar

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.Composable
import androidx.ui.animation.animate
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.ContextAmbient
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.RoundedCornerShape
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
import com.amar.modularnewsapp.ui.MainScreen
import com.amar.modularnewsapp.ui.Screen
import com.amar.modularnewsapp.ui.util.NavigationStack
import com.amar.modularnewsapp.ui.util.slideInTransition
import com.amar.modularnewsapp.ui.util.slideOutTransition

private const val POWERED = "Powered By NewsApiOrg"

@Composable
fun NavigationDrawer(
    onDrawerStateChange: (DrawerState) -> Unit,
    backgroundColor: Color,
    navigationStack: NavigationStack<MainScreen>
) {
    val context = ContextAmbient.current
    Column(modifier = Modifier.drawBackground(color = backgroundColor)) {
        Image(
            asset = imageResource(id = R.drawable.news_background),
            modifier = Modifier.preferredHeight(150.dp),
            contentScale = ContentScale.FillWidth
        )
        // TODO add selected shade
        ScrollableColumn(modifier = Modifier.padding(8.dp).weight(1f)) {
            DrawerButton(
                icon = R.drawable.ic_baseline_general_24,
                label = Screen.GENERAL.name,
                isSelected = navigationStack.current() == MainScreen.General,
                action = {
                    navigationStack.back()
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
                isSelected = navigationStack.current() == MainScreen.Business,
                action = {
//                    makeToast(context)
                    navigationStack.next(
                        next = MainScreen.Business,
                        enterTransition = slideInTransition,
                        exitTransition = slideOutTransition
                    )
                    onDrawerStateChange(DrawerState.Closed)
                }
            )
            DrawerButton(
                icon = R.drawable.ic_baseline_sport_24,
                label = Screen.SPORTS.name,
                isSelected = navigationStack.current() == MainScreen.Sports,
                action = {
//                    makeToast(context)
                    navigationStack.next(
                        next = MainScreen.Sports,
                        enterTransition = slideInTransition,
                        exitTransition = slideOutTransition
                    )
                    onDrawerStateChange(DrawerState.Closed)
                }
            )
            DrawerButton(
                icon = R.drawable.ic_baseline_entertainment_24,
                label = Screen.ENTERTAINMENT.name,
                isSelected = navigationStack.current() == MainScreen.Entertainment,
                action = {
//                    makeToast(context)
                    navigationStack.next(
                        next = MainScreen.Entertainment,
                        enterTransition = slideInTransition,
                        exitTransition = slideOutTransition
                    )
                    onDrawerStateChange(DrawerState.Closed)
                }
            )
            DrawerButton(
                icon = R.drawable.ic_baseline_health_24,
                label = Screen.HEALTH.name,
                isSelected = navigationStack.current() == MainScreen.Health,
                action = {
                    navigationStack.next(
                        next = MainScreen.Health,
                        enterTransition = slideInTransition,
                        exitTransition = slideOutTransition
                    )
                    onDrawerStateChange(DrawerState.Closed)
                }
            )

            DrawerButton(
                icon = R.drawable.ic_baseline_science_24,
                label = Screen.SCIENCE.name,
                isSelected = navigationStack.current() == MainScreen.Science,
                action = {
//                    makeToast(context)
                    navigationStack.next(
                        next = MainScreen.Science,
                        enterTransition = slideInTransition,
                        exitTransition = slideOutTransition
                    )
                    onDrawerStateChange(DrawerState.Closed)
                }
            )
            DrawerButton(
                icon = R.drawable.ic_baseline_technology_24,
                label = Screen.TECHNOLOGY.name,
                isSelected = navigationStack.current() == MainScreen.Technology,
                action = {
//                    makeToast(context)
                    navigationStack.next(
                        next = MainScreen.Technology,
                        enterTransition = slideInTransition,
                        exitTransition = slideOutTransition
                    )
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
        Column(modifier = Modifier.padding(8.dp)) {
            Divider(color = (MaterialTheme.colors).onSurface)
            Text(
                text = POWERED,
                modifier = Modifier.gravity(align = Alignment.CenterHorizontally)
                    .padding(8.dp),
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
    val textIconColor = animate(
        if (isSelected) {
            (MaterialTheme.colors).onSurface
        } else {
            ((MaterialTheme.colors).onSurface).copy(alpha = 0.6f)
        }
    )
    val backgroundColor = animate(
        if (isSelected) {
            (MaterialTheme.colors.surface).copy(alpha = 0.12f)
        } else {
            (MaterialTheme.colors).surface
        }
    )
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
                    style = (MaterialTheme.typography.body2).copy(
                        color = textIconColor
                    ),
                    modifier = Modifier.weight(1f).padding(end = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun SimpleImage(
    @DrawableRes id: Int, tint: Color? = null,
    modifier: Modifier = Modifier
) {
    val image = imageResource(id)
    if (tint != null) {
        Image(
            asset = image,
            modifier = modifier,
            colorFilter = ColorFilter(color = tint, blendMode = BlendMode.srcIn)
        )
    } else {
        Image(
            asset = image,
            modifier = modifier
        )
    }
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