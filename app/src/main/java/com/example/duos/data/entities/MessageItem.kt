package com.example.duos.data.entities

data class MessageItem(
    var from : String,
    var content : String,
    var sendTime : String,
    var viewType : Int  // 0일 시 왼쪽(상대가 보낸 메세지), 1일 시 중앙(~가 입장하셨습니다), 2일 시 오른쪽(내가 보낸 메세지)
)
