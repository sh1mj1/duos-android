package com.example.duos.data.local

import androidx.room.*
import com.example.duos.data.entities.LocalCategory

@Dao
interface LargeLocalDao {
    @Insert
    fun insert(localCategory: LocalCategory)

    @Update
    fun update(localCategory: LocalCategory)

    @Delete
    fun delete(localCategory: LocalCategory)

    @Insert
    fun insertAll(localCategoryList: List<LocalCategory>)

    @Query("SELECT * FROM LargeRegTable") // 테이블의 모든 값을 가져오기
    fun getLargeRegs(): List<LocalCategory>

    @Query("DELETE FROM LargeRegTable") // 테이블에 들어있는 모든 값을 지워라
    fun clearAll()

}