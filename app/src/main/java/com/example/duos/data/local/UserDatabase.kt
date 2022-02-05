package com.example.duos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.duos.data.entities.User
import com.example.duos.utils.Converters

@Database(entities = [User::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        private var instance: UserDatabase? = null

        @Synchronized
        fun getInstance(context: Context): UserDatabase? {
            if (instance == null) {
                synchronized(UserDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "room-database"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ) .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return instance
        }

    }
}