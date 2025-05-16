package com.example.demo.user

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/users")
    fun retrieveUser(
        @RequestParam email: String,
    ): UserResponse {
        return userService.retrieveUser(email).toUserResponse()
    }

    @PutMapping("/users")
    fun updateUserName(
        @RequestBody request: UpdateUserNameRequest,
    ): UserResponse {
        return userService.updateUserName(TEMP_USER_ID, request.newName).toUserResponse()
    }

    companion object {
        private const val TEMP_USER_ID = 1L
    }
}