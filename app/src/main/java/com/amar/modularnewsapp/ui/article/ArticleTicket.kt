package com.amar.modularnewsapp.ui.article

import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Clip
import androidx.ui.core.Text
import androidx.ui.core.dp
import androidx.ui.foundation.DrawImage
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.imageFromResource
import androidx.ui.layout.*
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import androidx.ui.material.withOpacity
import androidx.ui.res.imageResource
import com.amar.data.entities.NewsArticle
import com.amar.modularnewsapp.ui.common.Image
import com.amar.modularnewsapp.R

@Composable
fun ArticleTicket(
    backgroundColor: Color,
    article: NewsArticle
) {
    val typography = +MaterialTheme.typography()
    Surface(
        color = Color(0xFF2D2D2D),
        modifier = Spacing(16.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = ExpandedWidth wraps Spacing(8.dp)) {
            article.urlToImage?.let { imageUrl ->
                Container(modifier = ExpandedWidth, height = 100.dp) {
                    Clip(shape = RoundedCornerShape(4.dp)) {
                        Image(url = imageUrl, width = 100.dp, height = 100.dp)
//                        DrawImage(image = +imageResource(R.drawable.progress_loading))
                    }
                }
            }
            HeightSpacer(16.dp)
            Text(
                text = if (article.title == null) "Title not given" else article.title!!,
                style = typography.h6.withOpacity(0.87f)
            )
            Text(
                text = if (article.author == null) "Unknow author" else article.author!!,
                style = typography.body2.withOpacity(0.87f)
            )
            Text(
                text = article.publishedTime!!,
                style = typography.body2.withOpacity(0.6f)
            )
        }
    }
}