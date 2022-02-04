package com.example.duos.data.entities

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// 사용자 정보 데이터 클래스
@Entity(tableName = "UserTable")
data class User(
    var phoneNumber : String? = null,
    @PrimaryKey var nickName : String,
    var gender : Int? = null,
    var birthYear : Int? = null,
    var birthMonth : Int? = null,
    var birthDay : Int? = null,
    var locationCate : Int? = null,
    var location : Int? = null,
    var experience : Int? = null,
    var profileImg : Bitmap? = null,
    var introduce: String? = null
)
