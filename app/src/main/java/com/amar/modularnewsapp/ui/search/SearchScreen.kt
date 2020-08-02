package com.amar.modularnewsapp.ui.search

import android.content.Context
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onCommit
import androidx.compose.setValue
import androidx.ui.core.Alignment
import androidx.ui.core.ContextAmbient
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
import androidx.ui.material.icons.filled.ArrowBack
import androidx.ui.material.icons.filled.History
import androidx.ui.savedinstancestate.savedInstanceState
import androidx.ui.text.TextRange
import androidx.ui.unit.dp
import com.amar.modularnewsapp.ui.MainScreen
import com.amar.modularnewsapp.ui.common.AppBarType
import com.amar.modularnewsapp.ui.util.NavigationStack
import com.amar.modularnewsapp.viewmodel.ArticleModel

@Composable
fun SearchScreen(
    articleModel: ArticleModel,
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
                        Icon(Icons.Filled.ArrowBack)
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
            if(!it) {
                // call search
            }
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
private val historyStringSeperator = "$#%@!_:)"
@Suppress("DEPRECATION")
@Composable
private fun searchBarView(
    filterText: TextFieldValue,
    onFilter: (TextFieldValue) -> Unit,
    focus: FocusModifier,
    navigationStack: NavigationStack<MainScreen>
) {
    val context = ContextAmbient.current
    val pref = context.getSharedPreferences("searchHistory", Context.MODE_PRIVATE)
    val history = pref.getStringSet("histories", emptySet())

    var previosData by savedInstanceState {
        history!!.map {
            it.split(historyStringSeperator)[1]
        }
    }
    val data = previosData.filter { it.contains(filterText.text, ignoreCase = true) }

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
        // store max 6 last searched item
        val valueToSave = filterText.text
        if(history!!.size > 6) {
            val size = "6" + historyStringSeperator
            val temp = mutableSetOf<String>()
            for(i in history) {
                val index = i.split(historyStringSeperator)[0].toInt()
                val value = i.split(historyStringSeperator)[1]
                if(index != 1) {
                    temp.add((index-1).toString() + value)
                }
            }
            temp.add(size+valueToSave)
            history.clear()
            history.addAll(temp)
        } else {
            val size = history.size.toString() + historyStringSeperator
            history.add(size+valueToSave)
        }
        filterViewItem(navigationStack = navigationStack)
        val editor = pref.edit()
        editor.putStringSet("histories", history)
        editor.apply()
        editor.commit()
    }
}

@Composable
private fun filterViewItem(navigationStack: NavigationStack<MainScreen>) {

}
