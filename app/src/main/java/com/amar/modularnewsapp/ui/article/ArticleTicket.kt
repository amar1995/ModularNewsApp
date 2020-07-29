package com.amar.modularnewsapp.ui.article

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.core.clip
import androidx.ui.foundation.Box
import androidx.ui.foundation.Text
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.material.EmphasisAmbient
import androidx.ui.material.MaterialTheme
import androidx.ui.material.ProvideEmphasis
import androidx.ui.material.Surface
import androidx.ui.unit.dp
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.ui.common.UrlImage

@Composable
fun ArticleTicket(
    backgroundColor: Color,
    article: NewsArticle,
    modifier: Modifier = Modifier
) {
    val typography = MaterialTheme.typography
    Surface(
        color = backgroundColor,
        modifier = modifier.padding(16.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            article.urlToImage?.let { imageUrl ->
                Box(
                    modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.small)
                        .preferredHeight(100.dp)
                ) {
                    UrlImage(url = imageUrl, width = 100.dp, height = 100.dp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val emphasisLevels = EmphasisAmbient.current
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