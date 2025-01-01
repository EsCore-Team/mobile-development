package com.dicoding.escore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteEntity(
    @PrimaryKey val id: String?,
    val prevKey: Int?,
    val nextKey: Int?
)