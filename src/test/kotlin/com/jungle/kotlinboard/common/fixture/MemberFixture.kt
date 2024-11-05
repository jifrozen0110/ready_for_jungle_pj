package com.jungle.kotlinboard.common.fixture

import com.jungle.kotlinboard.domain.Member

fun createMember(
    id: Long = 1L,
    userId: String = "test",
    password: String = "password",
    nickName: String = "testNickName",
): Member =
    Member(
        id = id,
        userId = userId,
        password = password,
        nickName = nickName,
    )
