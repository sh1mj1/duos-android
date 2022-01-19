package com.example.duos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "LargeRegTable")
data class LocalCategory(
    @PrimaryKey
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String
    /*
    "code": "1100000000",
    "name": "서울특별시"
    */
)



