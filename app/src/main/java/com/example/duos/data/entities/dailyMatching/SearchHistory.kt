package com.example.duos.data.entities.dailyMatching

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
    @ColumnInfo(name = "keyword") val keyword: String?,
    @PrimaryKey(autoGenerate = true) val uid: Int = 0
)


data class SearchHistoryData(
    val keyword: String?
)