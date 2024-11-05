package com.jungle.kotlinboard.common.exception

class MemberNotFoundException(
    id: Long,
    message: String = "멤버를 찾을 수 없습니다.",
) : RuntimeException("$id $message")
