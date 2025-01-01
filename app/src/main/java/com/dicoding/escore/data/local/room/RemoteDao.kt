package com.dicoding.escore.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.escore.data.local.entity.RemoteEntity

@Dao
interface RemoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteEntity>?)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun getRemoteEntityId(id: String?): RemoteEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeys()
}