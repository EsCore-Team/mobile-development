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

    @Query("SELECT * FROM HistoryEntity WHERE email = :email ORDER BY createdAt DESC")
    fun getAllHistory(email: String): LiveData<List<HistoryEntity>>

    @Query("DELETE FROM HistoryEntity WHERE email = :email")
    fun deleteByEmail(email: String)
}