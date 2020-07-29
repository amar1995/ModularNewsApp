package com.amar.modularnewsapp.ui.util

import androidx.animation.TweenSpec
import androidx.compose.*
import androidx.ui.animation.animatedFloat
import androidx.ui.core.*
import androidx.ui.layout.Stack
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.reflect.KClass


data class Transition<T>(val data: T, val effect: CrossTransitionModifier?)

data class RelativeOffsetPxModifier(val x: Float = 0f, val y: Float = 0f) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints,
        layoutDirection: LayoutDirection
    ): MeasureScope.MeasureResult {
        val placeable = measurable.measure(constraints)
        return layout(placeable.width, placeable.height) {
            placeable.place((x * placeable.width).roundToInt(), (y * placeable.height).roundToInt())
        }
    }
}

val tweenAnimationSpec = TweenSpec<Float>(durationMillis = 500)

val slideRollRightTransition: CrossTransitionModifier = { progress, isInModifier ->
    val p = if (isInModifier) -(1f - progress) else progress
    RelativeOffsetPxModifier(x = p)
}
val slideRollLeftTransition: CrossTransitionModifier = { progress, isInModifier ->
    val p = if (isInModifier) 1f - progress else -progress
    RelativeOffsetPxModifier(x = p)
}
val slideInTransition: CrossTransitionModifier = { progress, isInModifier ->
    val p = if (isInModifier) 1f - progress else 0f
    val zIndex = if (isInModifier) 1f else 0f
    RelativeOffsetPxModifier(y = p).zIndex(zIndex)
}
val slideOutTransition: CrossTransitionModifier = { progress, isInModifier ->
    val p = if (isInModifier) 0f else progress
    val zIndex = if (isInModifier) 0f else 1f
    RelativeOffsetPxModifier(y = p).zIndex(zIndex)
}

val crossfadeTransition: CrossTransitionModifier = { progress, isInModifier ->
    val p = if (isInModifier) progress else 1f - progress
    Modifier.drawOpacity(p)
}
val fadeInTransition: CrossTransitionModifier = { progress, isInModifier ->
    val p = if (isInModifier) min(progress, .99f) else 1f
    val zIndex = if (isInModifier) 1f else 0f
    Modifier.zIndex(zIndex).drawOpacity(p)
}
val fadeOutTransition: CrossTransitionModifier = { progress, isInModifier ->
    val p = if (isInModifier) 1f else min(1f - progress, .99f)
    val zIndex = if (isInModifier) 0f else 1f
    Modifier.zIndex(zIndex).drawOpacity(p)
}

/**
 * This function returns the Modifier which is applied to both screens the current and the new one.
 * progress -> is the progress of the transition 0f..1f
 * isInModifier -> is true when the modifier is applied to the new screen
 *                 and false for the screen which is replaced by the new screen
 */
typealias CrossTransitionModifier = (progress: Float, isInModifier: Boolean) -> Modifier

private class CrossTransitionState<T> {
    var itemA: CrossTransitionAnimationItem<T>? = null
    var itemB: CrossTransitionAnimationItem<T>? = null
    var invalidate: () -> Unit = { }
    var effect: CrossTransitionModifier? = null
    private var isTargetA: Boolean = false
    val current get() = if (isTargetA) itemA else itemB
    fun newTarget(item: CrossTransitionAnimationItem<T>) {
        val old = current
        isTargetA = isTargetA.not()
        itemA = if (isTargetA) item else old
        itemB = if (isTargetA) old else item
    }

    fun releaseSource() {
        if (isTargetA)
            itemB = null
        else
            itemA = null
    }
}

private data class CrossTransitionAnimationItem<T>(val key: T, val transition: CrossTransition<T>)

private typealias CrossTransition<T> = @Composable() (data: T, children: @Composable() () -> Unit) -> Unit

@Composable
inline fun <reified T : Any> Route(
    transition: Transition<T>,
    noinline children: @Composable() (T) -> Unit
) = Route(T::class, transition, children)

@Composable
fun <T : Any> Route(
    clazz: KClass<T>,
    transition: Transition<T>,
    children: @Composable() (T) -> Unit
) {
    key(clazz) {
        val ts = remember { CrossTransitionState<T>() }
        val progress = animatedFloat(initVal = 0f)
        var isRunning by state { false }

        onCommit(transition) {
            ts.newTarget(CrossTransitionAnimationItem(transition.data) { data, children ->
                val effect = ts.effect
                val isTarget = data == ts.current?.key
                val modifier =
                    if (isRunning && effect != null) effect(progress.value, isTarget)
                    else Modifier
                Stack(modifier) {
                    children()
                }
            })
            if (ts.itemA?.key != ts.itemB?.key && transition.effect != null) {
                progress.snapTo(0f)
                isRunning = true
                progress.animateTo(targetValue = 1f, anim = tweenAnimationSpec, onEnd = { _, _ ->
                    ts.invalidate();
                    ts.releaseSource()
                    isRunning = false

                })
                ts.effect = transition.effect
            }
            if (ts.effect == null) {
                ts.releaseSource()
            }
            ts.invalidate()
        }

        @Composable
        fun showItem(item: CrossTransitionAnimationItem<T>?) {
            item?.let { (item, transition) ->
                key(item) {
                    transition(item) {
                        children(item)
                    }
                }
            }
        }
        Stack {
            ts.invalidate = invalidate
            showItem(item = ts.itemA)
            showItem(item = ts.itemB)
        }
    }
}