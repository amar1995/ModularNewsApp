package com.amar.modularnewsapp.ui.search

import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onCommit
import androidx.compose.setValue
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.focus.FocusDetailedState
import androidx.ui.core.focus.FocusModifier
import androidx.ui.foundation.*
import androidx.ui.input.ImeAction
import androidx.ui.input.KeyboardType
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.Column
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.layout.Row
import androidx.ui.layout.RowScope.gravity
import androidx.ui.layout.padding
import androidx.ui.layout.preferredSize
import androidx.ui.material.IconButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Close
import androidx.ui.material.icons.filled.History
import androidx.ui.savedinstancestate.savedInstanceState
import androidx.ui.text.TextRange
import androidx.ui.unit.dp
import com.amar.modularnewsapp.ui.MainScreen
import com.amar.modularnewsapp.ui.common.AppBarType
import com.amar.modularnewsapp.ui.util.NavigationStack

@Composable
fun SearchScreen(
    navigationStack: NavigationStack<MainScreen>
) {
    var filterText by savedInstanceState { TextFieldValue("") }
    // TODO change it with Modifier.focus()
    @Suppress("DEPRECATION")
    val focus = FocusModifier()
    Scaffold(
        topBar = {
            AppBarType(
                title = "",
                navigationIcon = {
                    IconButton(
                        modifier = Modifier.gravity(Alignment.CenterVertically),
                        onClick = {
                            navigationStack.back()
                        }) {
                        Icon(Icons.Filled.Close)
                    }
                },
                actions = {
                    filterField(
                        filterText = filterText,
                        onFilter = { filterText = it },
                        modifier = Modifier.gravity(Alignment.CenterHorizontally)
                            .weight(1f),
                        focus = focus
                    )
                }
            )
        }
    ) {
        searchBarView(
            filterText = filterText,
            onFilter = { filterText = it },
            focus = focus,
            navigationStack = navigationStack
        )
    }
}

@Suppress("DEPRECATION")
@Composable
private fun filterField(
    filterText: TextFieldValue,
    onFilter: (androidx.ui.input.TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    focus: FocusModifier
) {
    val nextFocus = FocusModifier()
    onCommit(focus) {
        if (filterText.text.isNullOrBlank())
            focus.requestFocus()
        else
            nextFocus.requestFocus()
    }
    TextField(
        value = filterText,
        onValueChange = onFilter,
        onFocusChanged = {

        },
        modifier = modifier + focus,
        cursorColor = MaterialTheme.colors.onSurface,
        keyboardType = KeyboardType.Ascii,
        imeAction = ImeAction.Search,
        onImeActionPerformed = {
            nextFocus.requestFocus()
        },
        textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface)
    )
    Text(text = "", modifier = nextFocus)
}

@Suppress("DEPRECATION")
@Composable
private fun searchBarView(
    filterText: TextFieldValue,
    onFilter: (TextFieldValue) -> Unit,
    focus: FocusModifier,
    navigationStack: NavigationStack<MainScreen>
) {
    var previosData by savedInstanceState {
        mutableListOf<String>("label:")
    }
    val data = previosData
        .filter { it.contains(filterText.text, ignoreCase = true) }

    if (focus.focusDetailedState == FocusDetailedState.Active || filterText.text.isNullOrBlank()) {
        ScrollableColumn() {
            for (i in data) {
                Column() {
                    Row(modifier = Modifier.gravity(Alignment.CenterHorizontally).padding(16.dp)) {
                        Icon(
                            asset = Icons.Filled.History,
                            modifier = Modifier.preferredSize(32.dp).padding(end = 16.dp)
                                .gravity(Alignment.CenterVertically)
                        )
                        Text(
                            text = i,
                            modifier = Modifier.gravity(Alignment.CenterVertically)
                                .clickable(onClick = {
                                    onFilter(
                                        TextFieldValue(
                                            text = i,
                                            selection = TextRange(i.length, i.length)
                                        )
                                    )
                                })
                        )
                    }
                }
            }
        }
    } else {
        // TODO save history
        filterViewItem(navigationStack = navigationStack)
    }
}

@Composable
private fun filterViewItem(navigationStack: NavigationStack<MainScreen>) {

}
