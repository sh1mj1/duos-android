package com.example.duos.utils

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModel : ViewModel() {

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

    var editProfileSetNickname : MutableLiveData<Boolean> = MutableLiveData(false)

    var editProfileNickname = MutableLiveData<String>()

    var editProfileLocationCate = MutableLiveData<Int?>()
    var editProfileLocation = MutableLiveData<Int?>()
    var editProfileLocationCateName = MutableLiveData<String?>()
    var editProfileLocationName = MutableLiveData<String?>()
    var editProfileLocationDialogShowing : MutableLiveData<Boolean> = MutableLiveData(false)

    var editProfileIntroduce = MutableLiveData<String>()
    var editProfileExperience = MutableLiveData<Int?>()

    var appointmentReviewContent = MutableLiveData<String>()



    // Appointment
    var isAppointmentExist : MutableLiveData<Boolean> = MutableLiveData(false)


}