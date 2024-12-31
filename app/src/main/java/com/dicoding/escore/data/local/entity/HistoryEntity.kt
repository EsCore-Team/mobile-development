package com.dicoding.escore.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity (tableName = "history")
@Parcelize
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "score")
    val score: String
) : Parcelable