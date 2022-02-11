package com.example.duos.data.entities.duplicate

import com.example.duos.data.remote.duplicate.DuplicateNicknameResponse

interface DuplicateNicknameListView {

    fun onGetDuplicateNicknameSuccess(duplicateNicknameResponse: DuplicateNicknameResponse)
    fun onGetDuplicateNicknameFailure(code : Int, message : String)
}