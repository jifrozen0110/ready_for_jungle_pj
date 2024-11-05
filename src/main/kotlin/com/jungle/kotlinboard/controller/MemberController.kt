package com.jungle.kotlinboard.controller

import com.jungle.kotlinboard.common.BaseResponse
import com.jungle.kotlinboard.common.auth.TokenInfo
import com.jungle.kotlinboard.common.utils.SecurityUtils
import com.jungle.kotlinboard.service.LoginDto
import com.jungle.kotlinboard.service.MemberRequest
import com.jungle.kotlinboard.service.MemberResponse
import com.jungle.kotlinboard.service.MemberService
import com.jungle.kotlinboard.service.MemberUpdateRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberService: MemberService,
) {
    @GetMapping("/info")
    fun getMember(): BaseResponse<MemberResponse> {
        val userId =
            SecurityUtils.userId()
        return BaseResponse(data = memberService.getMember(userId))
    }

    @DeleteMapping("")
    fun deleteMember(): BaseResponse<Unit> {
        val userId =
            SecurityUtils.userId()
        return BaseResponse(message = memberService.deleteMember(userId))
    }

    @PostMapping("/signup")
    fun signupMember(
        @RequestBody @Valid memberCreateRequest: MemberRequest,
    ): BaseResponse<Unit> {
        val resultMsg: String = memberService.createMember(memberCreateRequest)
        return BaseResponse(message = resultMsg)
    }

    @PatchMapping("/info")
    fun updateMember(
        @RequestBody memberUpdateRequest: MemberUpdateRequest,
    ): BaseResponse<Unit> {
        val userId =
            SecurityUtils.userId()
        return BaseResponse(message = memberService.updateMember(userId, memberUpdateRequest))
    }

    @PostMapping("/login")
    fun login(
        @RequestBody @Valid loginDto: LoginDto,
    ): BaseResponse<TokenInfo> {
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }
}
