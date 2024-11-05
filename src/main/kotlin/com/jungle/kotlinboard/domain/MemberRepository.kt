package com.jungle.kotlinboard.domain

import com.jungle.kotlinboard.common.auth.Role
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findMemberByUserId(userId: String): Member?
}

interface MemberRoleRepository : JpaRepository<MemberRole, Long> {
    fun deleteMemberRolesByMember(member: Member)

    fun existsByMemberAndRole(
        member: Member,
        role: Role,
    ): Boolean
}
