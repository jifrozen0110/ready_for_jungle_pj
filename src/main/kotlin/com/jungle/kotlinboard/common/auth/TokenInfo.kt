package com.jungle.kotlinboard.common.auth

data class TokenInfo(
    val grantType: String, // jwt 인증 권한 타입
    val accessToken: String,
)
