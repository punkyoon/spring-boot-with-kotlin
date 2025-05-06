package com.example.demo.user

import com.example.demo.common.AuthContext
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(
    private val accountService: AccountService,
) {
    @PostMapping("/auth/sign-in")
    fun signIn(
        @RequestBody request: SignInRequest,
    ): AuthResponse {
        val jwtAuthentication = accountService.signIn(request.username, request.password)

        return AuthResponse(
            accessToken = jwtAuthentication.accessToken,
            refreshToken = jwtAuthentication.refreshToken,
        )
    }

    @PostMapping("/auth/sign-up")
    fun signUp(
        @RequestBody request: SignUpRequest,
    ): AuthResponse {
        val jwtAuthentication = accountService.createAccount(
            loginUsername = request.loginUsername,
            loginPassword = request.loginPassword,
            name = request.name,
            email = request.email,
        )

        return AuthResponse(
            accessToken = jwtAuthentication.accessToken,
            refreshToken = jwtAuthentication.refreshToken,
        )
    }

    @PostMapping("/auth/refresh")
    fun refresh(
        @RequestBody request: RefreshTokenRequest,
    ): AuthResponse {
        val jwtAuthentication = accountService.refresh(request.refreshToken)

        return AuthResponse(
            accessToken = jwtAuthentication.accessToken,
            refreshToken = jwtAuthentication.refreshToken,
        )
    }

    @PutMapping("/auth/accounts/password")
    fun updatePassword(
        authContext: AuthContext,
        @RequestBody request: UpdatePasswordRequest,
    ) {
        accountService.updatePassword(authContext.userId, request.newPassword)
    }

    @DeleteMapping("/auth/accounts")
    fun deleteAccount(
        authContext: AuthContext,
    ) {
        accountService.deleteAccount(authContext.userId)
    }
}