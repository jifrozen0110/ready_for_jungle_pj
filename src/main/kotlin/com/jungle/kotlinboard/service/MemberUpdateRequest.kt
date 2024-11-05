package com.jungle.kotlinboard.service

data class MemberUpdateRequest(
    val password: String,
    val nickName: String,
)
