package com.example.demo.user

import com.example.demo.auth.JWTAuthentication
import com.example.demo.auth.JWTService
import com.example.demo.exception.NotFoundException
import com.example.demo.exception.ValidationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val jwtService: JWTService,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun createAccount(
        loginUsername: String,
        loginPassword: String,
        name: String,
        email: String,
    ): JWTAuthentication {
        val userEntity = userRepository.save(UserEntity(name, email))
        val accountEntity = AccountEntity(
            user = userEntity,
            password = passwordEncoder.encode(loginPassword),
            username = loginUsername,
        )

        accountRepository.save(accountEntity)

        return JWTAuthentication(
            accessToken = jwtService.generateAccessToken(userEntity),
            refreshToken = jwtService.generateRefreshToken(userEntity),
        )
    }

    @Transactional(readOnly = true)
    fun signIn(loginUsername: String, loginPassword: String): JWTAuthentication {
        val accountEntity = accountRepository.findByUsername(loginUsername)
            ?: throw NotFoundException("Account not found")

        if (!passwordEncoder.matches(loginPassword, accountEntity.password)) {
            throw ValidationException("Password does not match login password")
        }

        val userEntity = userRepository.findById(accountEntity.userId!!)
            .orElseThrow { NotFoundException("User not found") }

        return JWTAuthentication(
            accessToken = jwtService.generateAccessToken(userEntity),
            refreshToken = jwtService.generateRefreshToken(userEntity),
        )
    }

    @Transactional(readOnly = true)
    fun refresh(refreshToken: String): JWTAuthentication {
        val email = jwtService.validateToken(refreshToken)

        val userEntity = userRepository.findByEmail(email)
            ?: throw NotFoundException("Account not found")

        return JWTAuthentication(
            accessToken = jwtService.generateAccessToken(userEntity),
            refreshToken = jwtService.generateRefreshToken(userEntity),
        )
    }

    @Transactional
    fun updatePassword(userId: Long, newPassword: String) {
        val accountEntity = accountRepository.findById(userId)
            .orElseThrow { NotFoundException("User not found") }

        accountEntity.updatePassword(
            passwordEncoder.encode(newPassword)
        )

        accountRepository.save(accountEntity)
    }

    @Transactional
    fun deleteAccount(userId: Long) {
        accountRepository.deleteById(userId)
        userRepository.deleteById(userId)
    }
}