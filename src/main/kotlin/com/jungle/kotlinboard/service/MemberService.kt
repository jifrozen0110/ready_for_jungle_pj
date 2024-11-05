package com.jungle.kotlinboard.service

import com.jungle.kotlinboard.common.auth.JwtTokenProvider
import com.jungle.kotlinboard.common.auth.Role
import com.jungle.kotlinboard.common.auth.TokenInfo
import com.jungle.kotlinboard.common.exception.InvalidInputException
import com.jungle.kotlinboard.common.exception.MemberNotFoundException
import com.jungle.kotlinboard.domain.CustomUser
import com.jungle.kotlinboard.domain.Member
import com.jungle.kotlinboard.domain.MemberRepository
import com.jungle.kotlinboard.domain.MemberRole
import com.jungle.kotlinboard.domain.MemberRoleRepository
import org.apache.coyote.BadRequestException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    @Transactional(readOnly = true)
    fun getMember(id: Long): MemberResponse = memberRepository.findById(id).orElseThrow { MemberNotFoundException(id) }.toDto()

    fun findMember(id: Long): Member = memberRepository.findById(id).orElseThrow { MemberNotFoundException(id) }

    fun createMember(memberCreateRequest: MemberRequest): String {
        var member: Member? = memberRepository.findMemberByUserId(memberCreateRequest.userId)

        if (member != null) {
            throw InvalidInputException("이미 등록된 ID 입니다.")
        }

        member = memberRepository.save(memberCreateRequest.toEntity())

        val memberRole: MemberRole = MemberRole(null, Role.MEMBER, member)
        memberRoleRepository.save(memberRole)

        return "회원가입이 완료되었습니다."
    }

    fun updateMember(
        id: Long,
        memberUpdateRequest: MemberUpdateRequest,
    ): String {
        val member =
            memberRepository.findById(id).orElseThrow {
                MemberNotFoundException(id)
            }

        if (!member.checkPassword(memberUpdateRequest.password)) {
            throw BadRequestException("비밀번호가 일치하지 않습니다.")
        }

        member.updateNickname(memberUpdateRequest.nickName)

        return "수정완료"
    }

    fun deleteMember(id: Long): String {
        val member =
            memberRepository.findById(id).orElseThrow {
                MemberNotFoundException(id)
            }
        memberRoleRepository.deleteMemberRolesByMember(member)
        memberRepository.delete(member)
        return "삭제 완료"
    }

    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.userId, loginDto.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        return jwtTokenProvider.createToken(authentication)
    }

    fun getCurrentName(): String {
        val id =
            (
                SecurityContextHolder
                    .getContext()
                    .authentication
                    .principal as CustomUser
            ).userId
        val member =
            memberRepository.findById(id).orElseThrow {
                MemberNotFoundException(id)
            }
        return member.nickName
    }
}
