package com.dicoding.escore.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.escore.data.local.entity.HistoryEntity
import com.dicoding.escore.data.local.entity.RemoteEntity


@Database(
    entities = [HistoryEntity::class , RemoteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun remoteDao(): RemoteDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): HistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java, "history_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}