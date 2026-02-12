package com.example.camvidgram.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.camvidgram.data.local.dao.CommentDao
import com.example.camvidgram.data.local.dao.PostDao
import com.example.camvidgram.data.local.dao.UserDao
import com.example.camvidgram.data.local.entities.CommentEntity
import com.example.camvidgram.data.local.entities.PostEntity
import com.example.camvidgram.data.local.entities.UserEntity

@Database(
    entities = [UserEntity::class, PostEntity::class, CommentEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "camvidgram_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}