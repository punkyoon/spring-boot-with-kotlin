package com.example.demo.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.Objects

@Entity
@Table(name = "users")
class UserEntity(
    name: String,
    @Column(name = "email") val email: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set

    @Column(name = "name")
    var name: String = name
        private set

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: Instant = Instant.now()
        private set

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now()
        private set

    fun updateName(name: String) {
        this.name = name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserEntity) return false

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun toString(): String {
        return "UserEntity(id=$id, name='$name', email='$email', createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}