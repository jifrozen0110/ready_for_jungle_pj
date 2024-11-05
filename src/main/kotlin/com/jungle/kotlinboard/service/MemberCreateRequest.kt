package com.jungle.kotlinboard.service

import com.jungle.kotlinboard.domain.Member

data class MemberCreateRequest(
    val userId: String,
    val password: String,
    val nickName: String,
) {
    fun toEntity(): Member =
        Member(
            userId = this.userId,
            password = this.password,
            nickName = this.nickName,
        )
}
