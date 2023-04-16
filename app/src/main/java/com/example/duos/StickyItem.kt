package com.example.duos

import com.example.duos.data.entities.RecommendedPartner

data class StickyItem(
    var stickyId : String,
    var recommendedPartner: RecommendedPartner,
    var isSticky : Boolean
)
