package com.example.duos.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.duos.data.entities.chat.ChatMessage

@Dao
interface ChatMessageDao {
    @Insert
    fun insert(chatMessage: ChatMessage)

    @Delete
    fun delete(chatMessage: ChatMessage)

    @Query("SELECT * FROM ChatMessageTable WHERE chatMessageIdx LIKE :chatRoomIdx || '%'")
    fun getChatMessages(chatRoomIdx: String): List<ChatMessage>
}