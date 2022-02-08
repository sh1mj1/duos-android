package com.example.duos.ui.main.mypage.appointment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppointmentViewModel : ViewModel() {

    // 내부에서 설정하는 자료형은 Mutable 로 변경가능하도록 설정
    private val _previousGame = MutableLiveData<Int>()

    // 데이터를 직접 접근하지 않고 뷰모델을 통해 가져올 수 있도록 설정
    val previousGame : LiveData<Int>
    get() = _previousGame

    // 초기값 설정
    init {
        _previousGame.value = 0
    }

    fun updateView(count: Int){
        _previousGame.value = count
    }

}