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

