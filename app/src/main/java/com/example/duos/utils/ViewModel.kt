package com.example.duos.utils

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {

    // friendListCount
    var friendCount = MutableLiveData<Int?>()

    // signup
    var phoneNumber = MutableLiveData<String?>()
    var phoneNumberVerifying = MutableLiveData<String>()
    var signUp01Avail : MutableLiveData<Boolean> = MutableLiveData(false)
    var signUp02Avail : MutableLiveData<Boolean> = MutableLiveData(false)
    var signUp03Avail : MutableLiveData<Boolean> = MutableLiveData(false)
    var signUp04Avail : MutableLiveData<Boolean> = MutableLiveData(false)
    var agreementAll : MutableLiveData<Boolean> = MutableLiveData(false)
    var agreementEssential01 : MutableLiveData<Boolean> = MutableLiveData(false)
    var agreementEssential02 : MutableLiveData<Boolean> = MutableLiveData(false)

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

    // login
    var loginPhoneNumber = MutableLiveData<String?>()
    var loginPhoneNumberVerifying = MutableLiveData<String>()

    // partnerFilter
    var partnerLocationCate = MutableLiveData<Int?>()
    var partnerLocation = MutableLiveData<Int?>()
    var partnerLocationCateName = MutableLiveData<String?>()
    var partnerLocationName = MutableLiveData<String?>()
    var partnerLocationDialogShowing : MutableLiveData<Boolean> = MutableLiveData(false)
    var partnerGender = MutableLiveData<Int?>()

    // editProfile

//    var editProfileSetNickname : MutableLiveData<Boolean> = MutableLiveData(false)

    var editProfileNickname = MutableLiveData<String?>()

    var editProfileLocationCate = MutableLiveData<Int?>()
    var editProfileLocation = MutableLiveData<Int?>()
    var editProfileLocationCateName = MutableLiveData<String?>()
    var editProfileLocationName = MutableLiveData<String?>()
    var editProfileLocationDialogShowing : MutableLiveData<Boolean> = MutableLiveData(false)

    var editProfileIntroduce = MutableLiveData<String?>()
    var editProfileExperience = MutableLiveData<Int?>()
    var editProfileImg = MutableLiveData<Bitmap?>()

    var setEditProfileNickName : MutableLiveData<Boolean> = MutableLiveData(false)
    var setEditProfileImgUrl : MutableLiveData<Boolean> = MutableLiveData(false)
    var setEditProfileLocation : MutableLiveData<Boolean> = MutableLiveData(false)
    var setEditProfileIntroduction : MutableLiveData<Boolean> = MutableLiveData(false)
    var setEditProfileExperience : MutableLiveData<Boolean> = MutableLiveData(false)
    var setEditProfileIsDuplicated : MutableLiveData<Boolean> = MutableLiveData(false)
    var isChangedNickname : MutableLiveData<Boolean> = MutableLiveData(false)
    var setEditProfileNonPic : MutableLiveData<Boolean> = MutableLiveData(false)

    // 수정 조건 1
    var isValidNicknameEditCondition : MutableLiveData<Boolean> = MutableLiveData(false)



    // Appointment
    var isAppointmentExist : MutableLiveData<Boolean> = MutableLiveData(false)

    // jwt 재발급
    var jwtRefreshSuccess :  MutableLiveData<Boolean> = MutableLiveData(false)

    // 데일리 매칭
    var dailyMatchingTitle = MutableLiveData<String?>()
    var dailyMatchingPlace = MutableLiveData<String?>()
    var dailyMatchingContent = MutableLiveData<String?>()
    var dailyMatchingDate : MutableLiveData<Boolean> = MutableLiveData(false)
    var dailyMatchingTime : MutableLiveData<Boolean> = MutableLiveData(false)

    var dailyMatchingImg01 : MutableLiveData<Boolean> = MutableLiveData(false)
    var dailyMatchingImg02 : MutableLiveData<Boolean> = MutableLiveData(false)
    var dailyMatchingImg03 : MutableLiveData<Boolean> = MutableLiveData(false)
    var dailyMatchingImg01Bitmap = MutableLiveData<Bitmap?>()
    var dailyMatchingImg02Bitmap = MutableLiveData<Bitmap?>()
    var dailyMatchingImg03Bitmap = MutableLiveData<Bitmap?>()
    var dailyMatchingImgCount = MutableLiveData<Int?>()

    init {
        dailyMatchingImgCount.value = 0
    }

    companion object {
        private var instance: com.example.duos.utils.ViewModel? = null
        fun getInstance() = instance ?: synchronized(com.example.duos.utils.ViewModel::class.java) {
            instance ?:com.example.duos.utils.ViewModel().also { instance = it }
        }
    }

}