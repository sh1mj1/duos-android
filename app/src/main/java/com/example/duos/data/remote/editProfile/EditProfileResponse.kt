package com.example.duos.data.remote.editProfile

import com.example.duos.data.entities.editProfile.GetEditProfileResDto
import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result : GetEditProfileResDto

    )


/*

@SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: MyPageInfo
 */

/*
isSuccess	boolean
code	int
message	String
result	List
ㄴexistingProfileInfo	Object
ㄴuserIdx	int
ㄴprofileImgUrl	String
ㄴnickname	String
ㄴbirth	String
ㄴgender	int
ㄴintroduction	String
ㄴexperienceIdx	int
ㄴexperienceList	List
ㄴexperienceIdx	int
ㄴdescription	String
*/
