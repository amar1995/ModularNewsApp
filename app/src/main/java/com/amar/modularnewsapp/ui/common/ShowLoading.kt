package com.amar.modularnewsapp.ui.common

import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Box
import androidx.ui.foundation.ContentGravity
import androidx.ui.graphics.Color
import androidx.ui.layout.fillMaxSize
import androidx.ui.layout.wrapContentWidth
import androidx.ui.material.CircularProgressIndicator

@Composable
fun ShowLoading(color: Color = Color.Blue, opacity: Float = 1f) {
    Box(modifier = Modifier.fillMaxSize(), gravity = ContentGravity.Center) {
        CircularProgressIndicator(
            color = color,
            modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}