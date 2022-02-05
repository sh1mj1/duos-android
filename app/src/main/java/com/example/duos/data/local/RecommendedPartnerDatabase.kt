package com.example.duos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.duos.data.entities.RecommendedPartner

@Database(entities = [RecommendedPartner::class], version = 1, exportSchema = false)
abstract class RecommendedPartnerDatabase: RoomDatabase() {
    abstract fun recommendedPartnerDao(): RecommendedPartnerDao
    companion object {
        private var instance: RecommendedPartnerDatabase? = null

        @Synchronized
        fun getInstance(context: Context): RecommendedPartnerDatabase? {
            if (instance == null) {
                synchronized(RecommendedPartnerDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RecommendedPartnerDatabase::class.java,
                        "recommendedPartner-database"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ) .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }

            return instance
        }

    }
}