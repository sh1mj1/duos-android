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

    @Query("SELECT * FROM ChatMessageItemTable WHERE chatMessageIdx LIKE :chatRoomIdx || '%'")
    fun getChatMessages(chatRoomIdx: String): List<ChatMessageItem>
}