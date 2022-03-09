package com.example.duos.data.entities.dailyMatching

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.duos.data.entities.User
import com.example.duos.data.local.UserDao
import com.example.duos.utils.Converters
import com.example.duos.utils.ThreetenLocalDateTimeConverter
import com.google.android.datatransport.runtime.dagger.Provides
import com.google.gson.Gson
import javax.inject.Singleton

@Database(entities = [SearchHistory::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SearchHistoryDatabase : RoomDatabase() {
    abstract fun searchHistoryRoomDao() : SearchHistoryRoomDao
    companion object {
        private var instance : SearchHistoryDatabase? = null

        @Singleton
        @Provides
        fun provideGson(): Gson { return Gson() }
        @Synchronized
        fun getInstance(context: Context): SearchHistoryDatabase? {
            if (instance == null) {
                synchronized(SearchHistoryDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SearchHistoryDatabase::class.java,
                        "search-history-database"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ).fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()

                }
            }

            return instance
        }
    }
}
