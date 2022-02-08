package com.example.duos.data.local

import androidx.room.*
import com.example.duos.data.entities.chat.ChatRoom

@Dao
interface ChatRoomDao {
    @Insert
    fun insert(chatRoom: ChatRoom)

    @Update
    fun update(chatRoom: ChatRoom)

    @Delete
    fun delete(chatRoom: ChatRoom)

    @Query("SELECT * FROM ChatRoomTable")
    fun getChatRoomList(): List<ChatRoom>

    @Query("SELECT chatRoomIdx FROM ChatRoomTable WHERE chatRoomIdx =:chatRoomIdx")
    fun getChatRoomIdx(chatRoomIdx: String): String

}