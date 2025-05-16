package com.example.demo.config

import com.example.demo.auth.JWTService
import com.example.demo.exception.ErrorCode
import com.example.demo.exception.ErrorResponse
import com.example.demo.user.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.SecurityContextHolderFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtService: JWTService,
    private val userService: UserService,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/health", "/auth/sign-in", "/auth/sign-up", "/auth/refresh").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(RequestIdFilter(), SecurityContextHolderFilter::class.java)
            .addFilterBefore(JWTAuthenticationFilter(jwtService, userService), UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint { _, response, _ ->
                    val errorResponse = ErrorResponse(
                        code = ErrorCode.UNAUTHORIZED,
                        message = "Unauthorized request",
                    )

                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.contentType = MediaType.APPLICATION_JSON_VALUE

                    response.writer.write(ObjectMapper().writeValueAsString(errorResponse))
                }

                it.accessDeniedHandler { _, response, _ ->
                    val errorResponse = ErrorResponse(
                        code = ErrorCode.FORBIDDEN,
                        message = "Access denied",
                    )

                    response.status = HttpServletResponse.SC_FORBIDDEN
                    response.contentType = MediaType.APPLICATION_JSON_VALUE

                    response.writer.write(ObjectMapper().writeValueAsString(errorResponse))
                }
            }

        return http.build()
    }
}