package com.jungle.kotlinboard.common.auth

import com.jungle.kotlinboard.domain.CustomUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.util.Date

const val EXPIRATION_MILLISECONDS: Long = 1000 * 60 * 30

@Component
class JwtTokenProvider {
    @Value("\${jwt.secret}")
    lateinit var secretKey: String
    private val key by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    fun createToken(authentication: Authentication): TokenInfo {
        val authorities: String = authentication.authorities.joinToString(",", transform = GrantedAuthority::getAuthority)

        val now = Date()
        val accessExpiration = Date(now.time + EXPIRATION_MILLISECONDS)

        val accessToken =
            Jwts
                .builder()
                .subject(authentication.name)
                .claim("auth", authorities)
                .claim("userId", (authentication.principal as CustomUser).userId)
                .issuedAt(now)
                .expiration(accessExpiration)
                .signWith(key) // Algorithm is selected automatically based on the key
                .compact()

        return TokenInfo("Bearer", accessToken)
    }

    // 토큰에서 정보를 추출하는 함수
    fun getAuthetication(token: String): Authentication {
        val claims: Claims = getClaims(token)

        val auth = claims["auth"] ?: throw RuntimeException("잘못된 토큰입니다.")
        val userId = claims["userId"] ?: throw RuntimeException("잘못된 토큰입니다.")

        val authorities: Collection<GrantedAuthority> =
            (auth as String)
                .split(",")
                .map { SimpleGrantedAuthority(it) }

        val principal: UserDetails = CustomUser(userId.toString().toLong(), claims.subject, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    fun validationToken(token: String): Boolean {
        try {
            getClaims(token)
            return true
        } catch (e: Exception) {
            when (e) {
                is SecurityException -> {}
                is MalformedJwtException -> {}
                is ExpiredJwtException -> {}
                is UnsupportedJwtException -> {}
                is IllegalArgumentException -> {}
                else -> {}
            }
            println(e.message)
        }
        return false
    }

    private fun getClaims(token: String): Claims =
        Jwts
            .parser()
            .setSigningKey(key)
            .build()
            .parseSignedClaims(token)
            .body
}
