package com.dicoding.escore.data.local.room

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.escore.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<HistoryEntity>)

    @Query("SELECT * FROM history ORDER BY createdAt DESC")
    fun getAllStory(): PagingSource<Int, HistoryEntity>

    @Query("DELETE FROM history")
    suspend fun deleteAll()
}