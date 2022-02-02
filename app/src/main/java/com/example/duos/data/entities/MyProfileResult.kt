package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class MyProfileResult(

    @SerializedName("profileInfo") var profileInfo : MyProfileInfoItem,
    @SerializedName("reviews") val reviews: List<MyProfileReviewItem>

)
/*
result	List
    ㄴprofileInfo	Object
        ㄴuserIdx	int
        ㄴprofileImgUrl	String
        ㄴnickname	String
        ㄴage	String
        ㄴlocation	String
        ㄴrating	float
        ㄴintroduction	String
        ㄴexperience	String
        ㄴgamesCount	int
        ㄴreviewCount	int
*/

