package com.example.duos.data.entities.dailyMatching

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchHistory(
    @PrimaryKey val uid : Int?,
    @ColumnInfo(name = "keyword") val keyword: String?


)
