package com.example.duos.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpInfoViewModel : ViewModel() {

    var phoneNumber = MutableLiveData<String>()
    var phoneNumberVerifying = MutableLiveData<String>()
    var nickName = MutableLiveData<String>()
    var gender = MutableLiveData<String>()
    var birthYear = MutableLiveData<Int>() // 년
    var birthMonth = MutableLiveData<Int>() // 월
    var birthDay = MutableLiveData<Int>() // 일
    var setNickname : MutableLiveData<Boolean> = MutableLiveData(false)
    var setGender: MutableLiveData<Boolean> = MutableLiveData(false)
    var setBirth : MutableLiveData<Boolean> = MutableLiveData(false)
    var locationCate = MutableLiveData<Int>()
    var location = MutableLiveData<Int>()
    var locationCateName = MutableLiveData<String>()
    var locationName = MutableLiveData<String>()
    var locationDialogShowing : MutableLiveData<Boolean> = MutableLiveData(false)
    var experience = MutableLiveData<String>()
    var profileImg = MutableLiveData<Int>()
    var introduce = MutableLiveData<String>()

}