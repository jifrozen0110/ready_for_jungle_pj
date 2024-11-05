package com.jungle.kotlinboard.common.exception

class PostNotFoundException(
    id: Long,
    message: String = "게시글를 찾을 수 없습니다.",
) : RuntimeException("$id $message")
