package com.example.demo.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.Objects

@Entity
@Table(name = "accounts")
class AccountEntity(
    user: UserEntity,
    password: String,

    @Column(name = "username") val username: String,
) {
    @Id
    @Column(name = "user_id")
    val userId: Long? = user.id

    @Column(name = "password")
    var password: String = password
        private set

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: Instant = Instant.now()
        private set

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now()
        private set

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AccountEntity) return false

        return this.userId == other.userId
    }

    override fun hashCode(): Int {
        return Objects.hash(userId)
    }

    override fun toString(): String {
        return "AccountEntity(userId=$userId, username='$username', password=$password, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}