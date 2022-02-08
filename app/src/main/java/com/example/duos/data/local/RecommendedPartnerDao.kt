package com.example.duos.data.local

import androidx.room.*
import com.example.duos.data.entities.PartnerSearchData
import com.example.duos.data.entities.RecommendedPartner

@Dao
interface RecommendedPartnerDao {
    @Insert
    fun insert(recommendedPartner: RecommendedPartner)    // 최초 한번만 insert

    @Update
    fun update(recommendedPartner: RecommendedPartner)    // 매일 한 번만 update

    @Delete
    fun delete(recommendedPartner: RecommendedPartner)    // 회원탈퇴할때?

    @Query("SELECT * FROM RecommendedPartnerTable")
    fun getRecommendedPartnerList(): List<RecommendedPartner>

    @Query("DELETE FROM RecommendedPartnerTable")   // 모두 삭제
    fun deleteAll()
}