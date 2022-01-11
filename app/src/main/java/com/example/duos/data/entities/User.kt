package com.example.duos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// 사용자 정보 데이터 클래스
@Entity(tableName = "UserTable")
data class User(
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String,
    @SerializedName("sex") val sex: String
    // 지역, 구력, 프로필 사진 등 추가
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

