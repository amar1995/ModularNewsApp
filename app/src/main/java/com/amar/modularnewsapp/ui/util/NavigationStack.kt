package com.amar.modularnewsapp.ui.util

import androidx.compose.*


// TODO remember stack state
class BackStack<T> {
    private var data by mutableStateOf(mutableListOf<T>())

    fun clear() {
        data.clear()
    }

    fun add(value: T) {
        data.add(value)
    }

    val size = data.size

    val lastIndex: Int = data.size - 1

    fun last(): T {
        return data[size - 1]
    }

    fun removeAtLast() {
        data.removeAt(size - 1)
    }

    fun removeAtIndex(index: Int) {
        data.removeAt(index)
    }
}

class NavigationStack<T : Any>(
    val init: T,
    val defaultEnterTransition: CrossTransitionModifier? = null,
    val defaultExitTransition: CrossTransitionModifier? = null
) {

    private data class TransitionItem<T>(
        val item: T,
        val enterTransition: CrossTransitionModifier?,
        val exitTransition: CrossTransitionModifier?
    )

    private val backStackData = mutableListOf<TransitionItem<T>>()
    private lateinit var currentTransitionState: MutableState<Transition<T>>

    init {
        reset()
    }

    private fun reset() {
        backStackData.clear()
        backStackData.add(TransitionItem(init, defaultEnterTransition, defaultExitTransition))
        currentTransitionState = mutableStateOf(Transition(init, defaultEnterTransition))
    }

    /**
     * This function should only be used once for one NavigationStack instance.
     */
    @Composable
    fun getTransition(): Transition<T> {
        onDispose {
            reset()
        }
        return currentTransitionState.value
    }

    fun current(): T = currentTransitionState.value.data

    fun next(
        next: T,
        enterTransition: CrossTransitionModifier? = defaultEnterTransition,
        exitTransition: CrossTransitionModifier? = defaultExitTransition
    ) {
        backStackData.add(TransitionItem(next, enterTransition, exitTransition))
        currentTransitionState.value = Transition(next, enterTransition)
    }

    fun back(): T? = back(null, false)
    fun back(exitTransition: CrossTransitionModifier?): T? = back(exitTransition, true)
    private fun back(exitTransition: CrossTransitionModifier?, overrideTransition: Boolean): T? =
        if (backStackData.size <= 1) {
            null
        } else {
            val transition =
                if (overrideTransition) exitTransition else backStackData.last().exitTransition
            val previous = backStackData.last().item
            backStackData.removeAt(backStackData.lastIndex)
            val pData = backStackData.last().item
            currentTransitionState.value = Transition(pData, transition)
            previous
        }
}
