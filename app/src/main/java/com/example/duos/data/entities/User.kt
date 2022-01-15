package com.example.duos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// 사용자 정보 데이터 클래스
data class User(
    val phone: String,
    val password: String,
    val nickname: String,
    val sex: String
    // 지역, 구력, 프로필 사진 등 추가
)

