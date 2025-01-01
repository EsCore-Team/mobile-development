package com.dicoding.escore.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dicoding.escore.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: List<HistoryEntity>)

    @Query("SELECT * from HistoryEntity ORDER BY createdAt ASC")
    fun getAllHistory(): LiveData<List<HistoryEntity>>
}