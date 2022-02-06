package com.example.duos.utils

import com.example.duos.ApplicationClass.Companion.USER_IDX
import com.example.duos.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.example.duos.ApplicationClass.Companion.X_REFRESH_TOKEN
import com.example.duos.ApplicationClass.Companion.mSharedPreferences

fun saveJwt(jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString(X_ACCESS_TOKEN, jwtToken)
    editor.apply()
}

fun saveRefreshJwt(jwtToken: String) {
    val editor = mSharedPreferences.edit()
    editor.putString(X_REFRESH_TOKEN, jwtToken)
    editor.apply()
}

fun saveUserIdx(userIdx : Int){
    val editor = mSharedPreferences.edit()
    editor.putInt(USER_IDX, userIdx)
    editor.apply()
}

fun getAccessToken(): String? = mSharedPreferences.getString(X_ACCESS_TOKEN, null)

fun getRefreshToken(): String? = mSharedPreferences.getString(X_REFRESH_TOKEN, null)

fun getUserIdx(): Int? = mSharedPreferences.getInt(USER_IDX, 0)

fun saveFriendListDialogNotShowing(boolean: Boolean){
    // boolean 이 true 이면 더 이상 보여주지 않음,
    // boolean 이 false 이면 다음번에도 보여줌.

    val editor = mSharedPreferences.edit()
    editor.putBoolean("friendListDialogNotShowing", boolean)
    editor.apply()
}

fun getFriendListDiaglogNotShowing() : Boolean =
    mSharedPreferences.getBoolean("friendListDialogNotShowing", false)

