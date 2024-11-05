package com.jungle.kotlinboard.service

import com.fasterxml.jackson.annotation.JsonProperty
import com.jungle.kotlinboard.domain.Member
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class MemberRequest(
    val id: Long?,
    @field:NotBlank
    @JsonProperty("userId")
    @field:Pattern(
        regexp = "^[a-z0-9]{4,10}\$",
        message = "소문자, 숫자를 포함한 4~10자리로 입력.",
    )
    private val _userId: String?,
    @field:NotBlank
    @field:Pattern(
        regexp = "^[a-zA-Z0-9]{8,15}\$",
        message = "영문, 숫자를 포함한 8~15자리로 입력.",
    )
    @JsonProperty("password")
    private val _password: String?,
    @field:NotBlank
    @JsonProperty("nickname")
    private val _nickname: String?,
) {
    val userId: String
        get() = _userId!!
    val password: String
        get() = _password!!
    val nickname: String
        get() = _nickname!!

    fun toEntity(): Member =
        Member(
            userId = this.userId,
            password = this.password,
            nickName = this.nickname,
        )
}

data class LoginDto(
    @field:NotBlank
    @JsonProperty("userId")
    private val _userId: String?,
    @field:NotBlank
    @JsonProperty("password")
    private val _password: String?,
) {
    val userId: String
        get() = _userId!!
    val password: String
        get() = _password!!
}
