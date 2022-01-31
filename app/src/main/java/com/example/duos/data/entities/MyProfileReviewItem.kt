package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

// 내 프로필 페이지에 있는 후기 내용들.
data class MyProfileReviewItem(

    @SerializedName("reviewIdx") val reviewIdx : Int? = null,
    @SerializedName("writerIdx") val writerIdx : Int? = null,
    @SerializedName("writerNickname") val writerNickname : String? = "",
    @SerializedName("writerProfileImgUrl") val writerProfileImgUrl : String = "",
    @SerializedName("rating") val rating : Float? = null,
    @SerializedName("date") val date : String? = "",
    @SerializedName("reviewContent") val reviewContent : String = ""

    // 별 개수를 어떻게 할지???? 배열을 사용해서 인덱스 0 부터 4로 별의 값을 줘야 하나?
    // 평점이 만약 3.3이거나 2.2처럼 소수 첫번째 자리수가 간단하지 않을 경우??
    // 일단은 별들을 고정된 데이터라고 해두고 상의해보자.

)
