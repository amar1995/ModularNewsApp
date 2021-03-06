package com.amar.modularnewsapp.ui.article

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.foundation.isSystemInDarkTheme
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.EmphasisAmbient
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.Surface
import androidx.ui.unit.dp
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.ui.common.UrlImage
import com.amar.modularnewsapp.ui.dateToString
import java.util.*

@Composable
fun ArticleTicket(
    article: NewsArticle,
    modifier: Modifier = Modifier
) {
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colors
    Surface(
        color = if (isSystemInDarkTheme()) Color(0xff212121) else Color.White,
        modifier = modifier.padding(16.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.fillMaxWidth().plus(Modifier.padding(8.dp))) {
            article.urlToImage?.let { imageUrl ->
                if(!imageUrl.isNullOrBlank()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth()
                                + Modifier.preferredHeight(124.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        UrlImage(url = imageUrl, height = 100.dp, width = 100.dp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val emphasisLevels = EmphasisAmbient.current
            ProvideEmphasis(emphasisLevels.high) {
                Text(
                    text = if (article.title == null) "Title not given" else article.title!!,
                    color = colors.onSurface,
                    style = typography.h6
                )
            }
            ProvideEmphasis(emphasisLevels.high) {
                Text(
                    text = if (article.author == null) "Unknown author" else article.author!!,
                    color = colors.onSurface,
                    style = typography.body2
                )
            }
            ProvideEmphasis(emphasisLevels.medium) {
                Text(
                    text = if(article.publishedTime == null) dateToString(Date()) else dateToString(article.publishedTime!!),
                    color = colors.onSurface,
                    style = typography.body2
                )
            }
        }
    }
}