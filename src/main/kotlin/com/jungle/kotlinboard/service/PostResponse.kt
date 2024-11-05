package com.jungle.kotlinboard.service

import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val createdDate: LocalDateTime,
    val createdBy: String,
)
