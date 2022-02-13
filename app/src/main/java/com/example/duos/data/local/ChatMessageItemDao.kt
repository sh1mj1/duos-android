package com.example.duos.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.duos.data.entities.chat.ChatMessageItem

@Dao
interface ChatMessageItemDao {
    @Insert
    fun insert(chatMessageItem: ChatMessageItem)

    @Delete
    fun delete(chatMessageItem: ChatMessageItem)

    @Query("DELETE FROM ChatMessageItemTable WHERE chatRoomIdx =:chatRoomIdx")   // 모두 삭제
    fun deleteAll(chatRoomIdx: String)

//    @Query("SELECT * FROM ChatMessageItemTable WHERE chatMessageIdx LIKE :chatRoomIdx || '%'")
//    fun getChatMessages(chatRoomIdx: String): List<ChatMessageItem>

    @Query("SELECT * FROM ChatMessageItemTable WHERE chatRoomIdx = :chatRoomIdx")
    fun getChatMessages(chatRoomIdx: String): List<ChatMessageItem>

    @Query("SELECT * FROM ChatMessageItemTable WHERE chatRoomIdx =:chatRoomIdx ORDER BY ROWID DESC LIMIT 1")
    fun getLastMessageData(chatRoomIdx: String): ChatMessageItem
}