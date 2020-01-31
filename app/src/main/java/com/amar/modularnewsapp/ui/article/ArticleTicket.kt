package com.amar.modularnewsapp.ui.article

import androidx.compose.Composable
import androidx.ui.core.Clip
import androidx.ui.core.Text
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.surface.Surface
import androidx.ui.unit.dp
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.ui.common.Image

@Composable
fun ArticleTicket(
    backgroundColor: Color,
    article: NewsArticle
) {
    val typography = MaterialTheme.typography()
    Surface(
        color = Color(0xFF2D2D2D),
        modifier = LayoutPadding(16.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = LayoutWidth.Fill + LayoutPadding(8.dp)) {
            article.urlToImage?.let { imageUrl ->
                Container(modifier = LayoutWidth.Fill, height = 100.dp) {
                    Clip(shape = RoundedCornerShape(4.dp)) {
                        Image(url = imageUrl, width = 100.dp, height = 100.dp)
                    }
                }
            }
            Spacer(LayoutSize(16.dp))
            val emphasisLevels = MaterialTheme.emphasisLevels()
            ProvideEmphasis(emphasisLevels.high) {
                Text(
                    text = if (article.title == null) "Title not given" else article.title!!,
                    style = typography.h6
                )
            }
            ProvideEmphasis(emphasisLevels.high) {
                Text(
                    text = if (article.author == null) "Unknow author" else article.author!!,
                    style = typography.body2
                )
            }
            ProvideEmphasis(emphasisLevels.medium) {
                Text(
                    text = article.publishedTime!!,
                    style = typography.body2
                )
            }
        }
    }
}