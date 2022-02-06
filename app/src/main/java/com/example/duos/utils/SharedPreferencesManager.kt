package com.example.duos.utils

import com.example.duos.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.example.duos.ApplicationClass.Companion.mSharedPreferences

fun saveJwt(jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString(X_ACCESS_TOKEN, jwtToken)

    editor.apply()
}

fun getJwt(): String? = mSharedPreferences.getString(X_ACCESS_TOKEN, null)

fun saveFriendListDialogNotShowing(boolean: Boolean){
    // boolean 이 true 이면 더 이상 보여주지 않음,
    // boolean 이 false 이면 다음번에도 보여줌.

    val editor = mSharedPreferences.edit()
    editor.putBoolean("friendListDialogNotShowing", boolean)
    editor.apply()
}

fun getFriendListDiaglogNotShowing() : Boolean =
    mSharedPreferences.getBoolean("friendListDialogNotShowing", false)

fun saveCheckUserAppliedPartnerFilterMoreThanOnce(boolean: Boolean){
    val editor = mSharedPreferences.edit()
    editor.putBoolean("checkUserAppliedPartnerFilterMoreThanOnce", boolean)
    editor.apply()
}

fun getCheckUserAppliedPartnerFilterMoreThanOnce() : Boolean =
    mSharedPreferences.getBoolean("checkUserAppliedPartnerFilterMoreThanOnce", false)


// 필터 적용을 한번도 한 적이 없는 사용자의 경우, 매일 자정 파트너 추천(필터적용전) api를 호출하기 위해
// 마지막으로 api 호출한 날짜를 기억해서 현재 날짜와 다르면 api 호출한다. 같으면 재호출 안하고 룸디비의 목록 그대로 가져옴
fun saveLastUpdatedDate(lastUpdatedDate: String){
    val editor = mSharedPreferences.edit()
    editor.putString("lastUpdatedDate", lastUpdatedDate)
    editor.apply()
}

fun getLastUpdatedDate() : String =
    mSharedPreferences.getString("lastUpdatedDate", "1998-10-10").toString()