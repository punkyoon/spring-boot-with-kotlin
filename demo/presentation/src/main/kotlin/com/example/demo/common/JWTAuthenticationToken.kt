package com.example.demo.common

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JWTAuthenticationToken(
    private val authContext: AuthContext,
    private var isAuthenticated: Boolean = true,
) : Authentication {

    override fun getName(): String = this.authContext.name

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getCredentials(): Any {
        return "-"
    }

    override fun getDetails(): Any {
        return this.authContext.toString()
    }

    override fun getPrincipal(): Any {
        return this.authContext
    }

    override fun isAuthenticated(): Boolean {
        return this.isAuthenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.isAuthenticated = isAuthenticated
    }
}