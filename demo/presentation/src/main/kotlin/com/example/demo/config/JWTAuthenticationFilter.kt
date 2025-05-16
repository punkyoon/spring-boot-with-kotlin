package com.example.demo.config

import com.example.demo.auth.JWTService
import com.example.demo.common.AuthContext
import com.example.demo.common.JWTAuthenticationToken
import com.example.demo.exception.ErrorCode
import com.example.demo.exception.ErrorResponse
import com.example.demo.user.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JWTAuthenticationFilter(
    private val jwtService: JWTService,
    private val userService: UserService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authHeader = request.getHeader("Authorization")
            ?: return filterChain.doFilter(request, response)

        val bearerToken = if (authHeader.startsWith("Bearer ")) {
            authHeader.substring(7)
        } else {
            val errorResponse = ErrorResponse(
                code = ErrorCode.UNAUTHORIZED,
                message = "Invalid authorization header",
            )

            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.contentType = MediaType.APPLICATION_JSON_VALUE

            return response.writer.write(ObjectMapper().writeValueAsString(errorResponse))
        }

        val emailClaim = jwtService.validateToken(bearerToken)
        val userEntity = userService.retrieveUser(emailClaim)

        if (SecurityContextHolder.getContext().authentication == null) {
            val authContext = AuthContext(
                userId = userEntity.id!!,
                email = emailClaim,
                name = userEntity.name,
            )

            SecurityContextHolder.getContext().authentication = JWTAuthenticationToken(authContext)
        }

        if (!SecurityContextHolder.getContext().authentication.isAuthenticated) {
            SecurityContextHolder.getContext().authentication.isAuthenticated = true
        }

        filterChain.doFilter(request, response)
    }
}