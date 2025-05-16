package com.example.demo.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.demo.user.UserEntity
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.Duration

@Component
class JWTService(
    private val jwtProperties: JWTProperties,
) {

    fun validateToken(token: String): String {
        return JWT.require(Algorithm.HMAC256(jwtProperties.secretKey))
            .withIssuer(jwtProperties.issuer)
            .withAudience(jwtProperties.audience)
            .build()
            .verify(token)
            .getClaim(EMAIL_CLAIM)
            .asString()
    }

    fun generateAccessToken(userEntity: UserEntity): String {
        return this.generateToken(userEntity, jwtProperties.expiration.accessToken)
    }

    fun generateRefreshToken(userEntity: UserEntity): String {
        return this.generateToken(userEntity, jwtProperties.expiration.refreshToken)
    }

    private fun generateToken(
        userEntity: UserEntity,
        expiration: Duration,
    ): String {
        val now = Instant.now()

        return JWT.create()
            .withIssuer(jwtProperties.issuer)
            .withAudience(jwtProperties.audience)
            .withIssuedAt(now)
            .withExpiresAt(now.plus(expiration))
            .withClaim(NAME_CLAIM, userEntity.name)
            .withClaim(EMAIL_CLAIM, userEntity.email)
            .sign(Algorithm.HMAC256(jwtProperties.secretKey))
    }

    companion object {
        private const val NAME_CLAIM = "name"
        private const val EMAIL_CLAIM = "email"
    }
}