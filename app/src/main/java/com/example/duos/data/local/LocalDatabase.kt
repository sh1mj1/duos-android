package com.example.duos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.duos.data.entities.LocalCategory

@Database(entities = [LocalCategory::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun largeLocalDao(): LargeLocalDao
    companion object {
        private var instance: LocalDatabase? = null

        @Synchronized
        fun getInstance(context: Context): LocalDatabase? {
            if (instance == null) {
                synchronized(LocalDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        "room-databases"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ).allowMainThreadQueries().build()
                }
            }

            return instance
        }
    }
}