package com.example.duos.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.duos.data.entities.LocationCategoryList
import com.example.duos.data.entities.LocationList

class SignUpInfoViewModel : ViewModel() {

    var phoneNumber = MutableLiveData<String>()
    var nickName = MutableLiveData<String>()
    var gender = MutableLiveData<String>()
    var birthYear = MutableLiveData<Int>() // 년
    var birthMonth = MutableLiveData<Int>() // 월
    var birthDay = MutableLiveData<Int>() // 일
    var locationCate = MutableLiveData<Int>()
    var location = MutableLiveData<Int>()
    var locationCateName = MutableLiveData<String>()
    var locationName = MutableLiveData<String>()
    var locationDialogShowing : MutableLiveData<Boolean> = MutableLiveData(false)
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
    val localCateData : LiveData<Int>
        get() = locationCate
    val localData : LiveData<Int>
        get() = location
    val experienceData : LiveData<String>
        get() = experience
    val profileImgData : LiveData<Int>
        get() = profileImg
    val introduceData : LiveData<String>
        get() = introduce


}