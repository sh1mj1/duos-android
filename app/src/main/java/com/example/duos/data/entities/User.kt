package com.example.duos.data.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

// 사용자 정보 데이터 클래스
@Entity(tableName = "UserTable")
data class User(
    var phoneNumber : String? = null,
    @PrimaryKey var nickName : String,
    var gender : Int? = null,
    var birth: String? = null,
    var locationCate : Int? = null,
    var location : Int? = null,
    var experience : Int? = null,
    var profileImg : Bitmap? = null,
    var introduce: String? = null,
    var userIdx : Int? = null
)
