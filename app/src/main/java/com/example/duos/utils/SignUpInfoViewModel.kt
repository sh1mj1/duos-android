package com.example.duos.utils

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpInfoViewModel : ViewModel() {

    var phoneNumber = MutableLiveData<String>()
    var nickName = MutableLiveData<String>()
    var gender = MutableLiveData<String>()
    var birthYear = MutableLiveData<Int>() // 년
    var birthMonth = MutableLiveData<Int>() // 월
    var birthDay = MutableLiveData<Int>() // 일
    var localCate = MutableLiveData<String>()
    var local = MutableLiveData<String>()
    var experience = MutableLiveData<String>()
    var profileImg = MutableLiveData<Int>()
    var introduce = MutableLiveData<String>()

    // 다른 클래스가 접근할 수 있는 데이터
    val phoneNumberData : LiveData<String>
        get() = phoneNumber
    val nickNameData : LiveData<String>
        get() = nickName
    val genderData : LiveData<String>
        get() = gender
    val birthrYearData : LiveData<Int>
        get() = birthYear
    val birthMonthData : LiveData<Int>
        get() = birthMonth
    val birthDayData : LiveData<Int>
        get() = birthDay
    val localCateData : LiveData<String>
        get() = localCate
    val localData : LiveData<String>
        get() = local
    val experienceData : LiveData<String>
        get() = experience
    val profileImgData : LiveData<Int>
        get() = profileImg
    val introduceData : LiveData<String>
        get() = introduce

    fun setGender(genderValue: String){
        gender.value = genderValue
    }

}