package com.jungle.kotlinboard.common.utils

import com.jungle.kotlinboard.common.exception.InvalidTokenException
import com.jungle.kotlinboard.domain.CustomUser
import org.springframework.security.core.context.SecurityContextHolder

class SecurityUtils {
    companion object {
        fun userId(): Long {
            val authentication = SecurityContextHolder.getContext().authentication

            if (authentication == null || !authentication.isAuthenticated || authentication.principal !is CustomUser) {
                throw InvalidTokenException("토큰이 유효하지 않습니다.")
            }
            return (authentication.principal as CustomUser).userId
        }
    }
}
