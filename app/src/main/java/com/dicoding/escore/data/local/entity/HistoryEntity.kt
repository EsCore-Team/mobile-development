package com.dicoding.escore.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class HistoryEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "createdAt")
    var createdAt: String? = null,

    @ColumnInfo(name = "score")
    var score: String? = null
) : Parcelable