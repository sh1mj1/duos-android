package com.example.duos.data.local

import androidx.room.*
import com.example.duos.data.entities.LargeLocal

@Dao
interface LargeLocalDao {
    @Insert
    fun insert(largeLocal: LargeLocal)

    @Update
    fun update(largeLocal: LargeLocal)

    @Delete
    fun delete(largeLocal: LargeLocal)

    @Insert
    fun insertAll(largeLocalList: List<LargeLocal>)

    @Query("SELECT * FROM LargeRegTable") // 테이블의 모든 값을 가져오기
    fun getLargeRegs(): List<LargeLocal>

    @Query("DELETE FROM LargeRegTable") // 테이블에 들어있는 모든 값을 지워라
    fun clearAll()

}