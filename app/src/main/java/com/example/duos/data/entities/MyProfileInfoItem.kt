package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class MyProfileInfoItem(
    @SerializedName("userIdx") var userIdx: Int? = null,
    @SerializedName("profileImgUrl") var profileImgUrl: String = "",
    @SerializedName("nickname") var nickname: String = "",
    @SerializedName("age") var age: String = "",
    @SerializedName("location") var location: String = "",
    @SerializedName("rating") var rating: Float? = null,
    @SerializedName("introduction") var introduction: String = "",
    @SerializedName("experience") var experience: String = "",
    @SerializedName("gamesCount") var gamesCount: Int? = null,
    @SerializedName("reviewCount") var reviewCount: Int? = null

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

