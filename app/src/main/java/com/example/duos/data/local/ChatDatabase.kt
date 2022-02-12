package com.example.duos.data.local

import android.content.Context
import androidx.room.*
import com.example.duos.data.entities.chat.ChatMessageItem
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.utils.ThreetenLocalDateTimeConverter
import com.google.android.datatransport.runtime.dagger.Provides
import com.google.gson.Gson
import javax.inject.Singleton

@Database(entities = [ChatRoom::class, ChatMessageItem::class], version = 11, exportSchema = false)
@TypeConverters(
    value = [ThreetenLocalDateTimeConverter::class]
)
abstract class ChatDatabase: RoomDatabase() {
    abstract fun chatRoomDao(): ChatRoomDao
    abstract fun chatMessageItemDao(): ChatMessageItemDao
    companion object {
        private var instance: ChatDatabase? = null

        @Singleton
        @Provides
        fun provideGson(): Gson { return Gson() }

        @Synchronized
        fun getInstance(context: Context, gson: Gson): ChatDatabase? {
            if (instance == null) {
                synchronized(ChatDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ChatDatabase::class.java,
                        "chat-database"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ) .addTypeConverter(ThreetenLocalDateTimeConverter(gson))
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return instance
        }

    }
}