package com.amar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.amar.data.common.ConstantConfig.DATABASE_NAME
import com.amar.data.common.ConstantConfig.VERSION
import com.amar.data.common.DateConverter
import com.amar.data.dao.ArticleDao
import com.amar.data.entities.NewsArticle
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Database(entities = arrayOf(NewsArticle::class), version = VERSION)
@TypeConverters(DateConverter::class)
abstract class DatabaseClient: RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        private lateinit var databaseInstance: DatabaseClient

        // Use for migration
        private val MIGRATION_1_2: Migration = object: Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getInstance(context: Context): DatabaseClient {
            synchronized(DatabaseClient::class.java) {
                if (!::databaseInstance.isInitialized) {
                    databaseInstance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseClient::class.java,
                        DATABASE_NAME
                    ).build()
                }
            }
            return databaseInstance
        }
    }
}
