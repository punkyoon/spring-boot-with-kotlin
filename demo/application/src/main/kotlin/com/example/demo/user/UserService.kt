package com.example.demo.user

import com.example.demo.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun retrieveUser(email: String): UserEntity {
        return userRepository.findByEmail(email)
            ?: throw NotFoundException("User with email $email not found")
    }

    @Transactional
    fun updateUserName(userId: Long, name: String): UserEntity {
        val userEntity = userRepository.findById(userId).orElseThrow {
            NotFoundException("User not found")
        }

        userEntity.updateName(name)

        return userRepository.save(userEntity)
    }
}