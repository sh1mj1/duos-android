package com.example.duos.data.entities.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "ChatRoomTable")
data class ChatRoom(
    @SerializedName("chatRoomName") var chatRoomName : String = "",
    @SerializedName("chatRoomImageUrl") var chatRoomImg : String? = "",
    @SerializedName("participantIdx") var participantIdx : String = "",
    @SerializedName("lastMessage") var lastMessage : String = "",
    @SerializedName("updatedAt") var lastUpdatedAt : String = ""
){
    @PrimaryKey(autoGenerate = true)
    @SerializedName("chatRoomIdx")
    var chatRoomIdx : String = ""
//    var userIdx : Int = 0
}
