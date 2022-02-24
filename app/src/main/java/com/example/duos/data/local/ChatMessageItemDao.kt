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

    @Query("SELECT * FROM ChatMessageItemTable WHERE chatRoomIdx =:chatRoomIdx AND chatMessageId > :lastAddedChatMessageId")
    fun getUpdatedMessages(chatRoomIdx: String, lastAddedChatMessageId: Int): List<ChatMessageItem>

    @Query("SELECT * FROM ChatMessageItemTable WHERE chatRoomIdx =:chatRoomIdx ORDER BY ROWID DESC LIMIT 1")
    fun getLastMessageData(chatRoomIdx: String): ChatMessageItem

    @Query("DELETE FROM ChatMessageItemTable")  // 테이블에 있는 모든 값을 지워라
    fun clearAll()
}

/*
    @Query("DELETE FROM ChatRoomTable") // 테이블에 들어있는 모든 값을 지워라
    fun clearAll()
 */