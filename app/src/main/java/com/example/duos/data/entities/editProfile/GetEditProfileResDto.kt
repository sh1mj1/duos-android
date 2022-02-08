package com.example.duos.data.entities.editProfile

import com.google.gson.annotations.SerializedName

data class GetEditProfileResDto(
    @SerializedName("existingProfileInfo") var existingProfileInfo : ExistingProfileInfo
//    @SerializedName("experienceList") var experienceList : List<ExperienceList>


)


/*
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
