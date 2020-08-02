package com.amar.modularnewsapp.ui.common

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.layout.RowScope
import androidx.ui.layout.padding
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Surface
import androidx.ui.unit.dp


@Composable
fun AppBarType(
    title: String?,
    navigationIcon: @Composable() (() -> Unit)?,
    actions: @Composable() RowScope.() -> Unit = {}
) {
    CustomAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions
    )
}


@Composable
private fun FilterAppBar(
    navigationIcon: @Composable() (() -> Unit)?,
    actions: @Composable() (RowScope.() -> Unit)
) {
    androidx.ui.material.TopAppBar(
        title = { Text(text = "") },
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = TopAppBarElevation
    )
}


@Composable
fun SelectorAppBar(
    count: Int,
    navigationIcon: @Composable() (() -> Unit)?,
    actions: @Composable() (RowScope.() -> Unit)
) {
    // actions
    // 1. delete/restore
    // 2. read/unread
    // 3. on/off
    // 4. batch edit (labels, actions, device, schedule checks, config*)
    // 5. permanent delete
    androidx.ui.material.TopAppBar(
        title = { Text(text = count.toString()) },
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = TopAppBarElevation
    )
}

@Composable
private fun CustomAppBar(
    title: String? = null,
    navigationIcon: @Composable() (() -> Unit)?,
    actions: @Composable() (RowScope.() -> Unit)
) {
    // null
    if (title.isNullOrBlank()) {
        MainAppBar(
            navigationIcon = navigationIcon,
            actions = actions
        )
    } else {
        CommonAppBar(
            title = title,
            actions = actions,
            navigationIcon = navigationIcon
        )
    }
}

@Composable
private fun CommonAppBar(
    title: String,
    actions: @Composable() (RowScope.() -> Unit),
    navigationIcon: @Composable() (() -> Unit)?
) {
    androidx.ui.material.TopAppBar(
        title = { Text(text = title) },
        navigationIcon = navigationIcon,
        actions = actions,
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface,
        elevation = TopAppBarElevation
    )
}

@Composable
private fun MainAppBar(
    navigationIcon: @Composable() (() -> Unit)?,
    actions: @Composable() (RowScope.() -> Unit)
) {
    Surface(
        color = MaterialTheme.colors.surface,
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
        shape = RoundedCornerShape(6.dp),
        elevation = 3.dp
    ) {
        androidx.ui.material.TopAppBar(
            title = { Text(text = "") },
            navigationIcon = navigationIcon,
            actions = actions,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
            elevation = TopAppBarElevation
        )
    }
}


private val TopAppBarElevation = 4.dp