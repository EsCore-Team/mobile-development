package com.dicoding.escore.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.escore.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY createdAt DESC")
    fun getHistory(): LiveData<List<HistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertHistory(news: List<HistoryEntity>)

    @Update
    fun updateHistory(news: HistoryEntity)
}