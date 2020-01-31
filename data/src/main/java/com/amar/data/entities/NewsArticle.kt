package com.amar.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amar.data.common.ConstantConfig.TABLE_NAME
import com.google.gson.annotations.SerializedName

data class Source(
    @ColumnInfo(name = Column.sourceId)
    @SerializedName(GsonConst.sourceId)
    val id: String? = null,

    @ColumnInfo(name = Column.name)
    @SerializedName(GsonConst.name)
    val name: String? = null
) {
    object Column {
        const val sourceId = "source_id"
        const val name = "name"
        const val id = "id"
        const val author = "author"
        const val title = "title"
        const val description = "description"
        const val url = "url"
        const val imageUrl = "image_url"
        const val time = "time"
        const val content = "content"
        const val category = "category"
    }

    object GsonConst {
        const val sourceId = "id"
        const val name = "name"
        const val author = "author"
        const val title = "title"
        const val description = "description"
        const val url = "url"
        const val imageUrl = "urlToImage"
        const val time = "publishedAt"
        const val content = "content"
    }
}


@Entity(tableName = TABLE_NAME)
data class NewsArticle(
    @Embedded
    val source: Source?,

    @ColumnInfo(name = Source.Column.id)
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = Source.Column.author)
    @SerializedName(Source.GsonConst.author)
    val author: String?,

    @ColumnInfo(name = Source.Column.title)
    @SerializedName(Source.GsonConst.title)
    val title: String?,

    @ColumnInfo(name = Source.Column.description)
    @SerializedName(Source.GsonConst.description)
    val description: String?,

    @ColumnInfo(name = Source.Column.url)
    @SerializedName(Source.GsonConst.url)
    val url: String?,

    @ColumnInfo(name = Source.Column.imageUrl)
    @SerializedName(Source.GsonConst.imageUrl)
    val urlToImage: String?,

    @ColumnInfo(name = Source.Column.time)
    @SerializedName(Source.GsonConst.time)
    val publishedTime: String?,

    @ColumnInfo(name = Source.Column.content)
    @SerializedName(Source.GsonConst.content)
    val content: String?,

    @ColumnInfo(name = Source.Column.category)
    var category: String?
)