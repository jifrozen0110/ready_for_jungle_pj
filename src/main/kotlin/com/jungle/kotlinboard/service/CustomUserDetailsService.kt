package com.jungle.kotlinboard.service

import com.jungle.kotlinboard.domain.CustomUser
import com.jungle.kotlinboard.domain.Member
import com.jungle.kotlinboard.domain.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails =
        memberRepository
            .findMemberByUserId(username!!)
            ?.let { createUserDetails(it) } ?: throw UsernameNotFoundException("not found user")

    private fun createUserDetails(member: Member): UserDetails =
        CustomUser(
            member.id!!,
            member.userId,
            passwordEncoder.encode(member.password),
            member.memberRole!!.map { SimpleGrantedAuthority("ROLE_${it.role}") },
        )
}
