package com.example.duos.ui.signup.localSearch

import com.example.duos.data.entities.LocationCategoryList
import com.example.duos.data.entities.LocationList

interface LocationView {
    fun onLocationCategoryListOnView(locationCategoryList: List<LocationCategoryList>)
    fun onLocationListOnView(locatonList: List<LocationList>)
    fun onLocationFailure(code: Int, message: String)
}