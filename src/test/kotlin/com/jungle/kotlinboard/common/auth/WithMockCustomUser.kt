package com.jungle.kotlinboard.common.auth

import org.springframework.security.test.context.support.WithSecurityContext

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomUser(
    val userId: Long = 1L,
    val username: String = "username",
    val password: String = "password",
    val authorities: String = "MEMBER",
)
