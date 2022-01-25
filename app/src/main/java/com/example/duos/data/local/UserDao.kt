package com.example.duos.data.local

import androidx.room.*
import com.example.duos.data.entities.User

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM UserTable WHERE nickName = :nickName") // 닉네임에 해당하는 user 가져오기
    fun getUser(nickName : String): User

    @Query("DELETE FROM UserTable") // 테이블에 들어있는 모든 값을 지워라
    fun clearAll()

}