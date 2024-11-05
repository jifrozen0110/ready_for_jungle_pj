package com.jungle.kotlinboard.common.auth

import com.jungle.kotlinboard.domain.CustomUser
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory

class WithMockCustomUserSecurityContextFactory : WithSecurityContextFactory<WithMockCustomUser> {
    override fun createSecurityContext(customUser: WithMockCustomUser): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()
        val authorities: Collection<GrantedAuthority> =
            (customUser.authorities as String)
                .split(",")
                .map { SimpleGrantedAuthority(it.trim()) }
        val principal = CustomUser(customUser.userId, customUser.username, customUser.password, authorities)
        val auth: Authentication =
            UsernamePasswordAuthenticationToken(
                principal,
                "",
                authorities,
            )

        context.authentication = auth
        return context
    }
}
