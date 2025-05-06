package com.example.demo.goal

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.Objects

@Entity
@Table(name = "goal_progresses")
class GoalProgressEntity(
    @MapsId
    @OneToOne
    @JoinColumn(name = "goal_id", referencedColumnName = "id", insertable = false, updatable = false)
    val goal: GoalEntity
) {
    @Id
    @Column(name = "goal_id")
    val goalId: Long? = goal.id

    @Column(name = "progress_rate")
    var progressRate: Int = 0
        private set

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: Instant = Instant.now()
        private set

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now()
        private set

    internal val completed get(): Boolean = this.progressRate == MAX_PROGRESS_RATE

    internal fun checkProgress() {
        if (this.progressRate >= MAX_PROGRESS_RATE) {
            return
        }

        this.progressRate += 1
    }

    internal fun revertProgress() {
        if (this.progressRate <= MIN_PROGRESS_RATE) {
            return
        }

        this.progressRate -= 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GoalProgressEntity) return false

        return this.goalId == other.goalId
    }

    override fun hashCode(): Int {
        return Objects.hashCode(goalId)
    }

    override fun toString(): String {
        return "GoalProgressEntity(goalId=$goalId, progressRate=$progressRate, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

    companion object {
        private const val MIN_PROGRESS_RATE: Int = 0
        private const val MAX_PROGRESS_RATE: Int = 100
    }
}