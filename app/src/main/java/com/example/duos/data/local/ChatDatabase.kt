package com.example.duos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.duos.data.entities.chat.ChatMessageItem
import com.example.duos.data.entities.chat.ChatRoom

@Database(entities = [ChatRoom::class, ChatMessageItem::class], version = 10, exportSchema = false)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun chatRoomDao(): ChatRoomDao
    abstract fun chatMessageItemDao(): ChatMessageItemDao
    companion object {
        private var instance: ChatDatabase? = null

        @Synchronized
        fun getInstance(context: Context): ChatDatabase? {
            if (instance == null) {
                synchronized(ChatDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChatDatabase::class.java,
                        "chat-database"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ) .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return instance
        }

    }
}