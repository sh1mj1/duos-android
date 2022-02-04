package com.example.duos.utils

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpInfoViewModel : ViewModel() {

    var phoneNumber = MutableLiveData<String?>()
    var phoneNumberVerifying = MutableLiveData<String>()
    var signUp01Avail : MutableLiveData<Boolean> = MutableLiveData(false)
    var signUp02Avail : MutableLiveData<Boolean> = MutableLiveData(false)
    var signUp03Avail : MutableLiveData<Boolean> = MutableLiveData(false)
    var signUp04Avail : MutableLiveData<Boolean> = MutableLiveData(false)
    var signUp05Avail : MutableLiveData<Boolean> = MutableLiveData(false)

    var nickName = MutableLiveData<String?>()
    var gender = MutableLiveData<Int?>()
    var birthYear = MutableLiveData<Int?>() // 년
    var birthMonth = MutableLiveData<Int?>() // 월
    var birthDay = MutableLiveData<Int?>() // 일
    var setNickname : MutableLiveData<Boolean> = MutableLiveData(false)
    var setGender: MutableLiveData<Boolean> = MutableLiveData(false)
    var setBirth : MutableLiveData<Boolean> = MutableLiveData(false)
    var locationCate = MutableLiveData<Int?>()
    var location = MutableLiveData<Int?>()
    var locationCateName = MutableLiveData<String?>()
    var locationName = MutableLiveData<String?>()
    var locationDialogShowing : MutableLiveData<Boolean> = MutableLiveData(false)
    var experience = MutableLiveData<Int?>()
    var profileImg = MutableLiveData<Bitmap?>()
    var introduce = MutableLiveData<String?>()

}