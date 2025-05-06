package com.example.demo.user

data class UserResponse(
    val userId: Long,
    val name: String,
    val email: String,
)

fun UserEntity.toUserResponse(): UserResponse {
    return UserResponse(
        userId = this.id!!,
        name = this.name,
        email = this.email,
    )
}