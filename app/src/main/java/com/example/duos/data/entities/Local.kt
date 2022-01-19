package com.example.duos.data.entities

data class SmallLocal(
    val address02 : String, // 시/구/군
    var cityName : String // 어떤 시/도 에 속하는지 가리키는 변수 (foreign key 역할)
)
