package com.example.demo.goal

import com.example.demo.exception.ValidationException
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.time.LocalDate
import java.util.Objects

@Entity
@Table(name = "goals")
class GoalEntity(
    @Column(name = "user_id") val userId: Long,
    @Column(name = "title") var title: String,
    @Column(name = "description") var description: String,
    @Column(name = "start_date") var startDate: LocalDate? = null,
    @Column(name = "end_date") var endDate: LocalDate? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set

    @Column(name = "completed")
    var completed: Boolean = false
        private set

    @OneToOne(
        mappedBy = "goal",
        fetch = FetchType.EAGER,
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        optional = false,
    )
    var progress: GoalProgressEntity? = GoalProgressEntity(goal = this)
        private set

    @CreationTimestamp
    @Column(name = "created_at")
    var createdAt: Instant = Instant.now()
        private set

    @UpdateTimestamp
    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now()
        private set

    fun updateGoal(title: String?, description: String?, startDate: LocalDate?, endDate: LocalDate?) {
        val newStartDate = startDate ?: this.startDate
        val newEndDate = endDate ?: this.endDate

        if (newStartDate != null && newEndDate != null && newStartDate > newEndDate) {
            throw ValidationException("`startDate` must be before or equal to `endDate`")
        }

        this.startDate = startDate ?: this.startDate
        this.endDate = endDate ?: this.endDate

        this.title = title ?: this.title
        this.description = description ?: this.description
    }

    fun checkProgress() {
        this.progress?.checkProgress()
        this.completed = this.progress?.completed ?: false
    }

    fun revertProgress() {
        this.progress?.revertProgress()
        this.completed = this.progress?.completed ?: false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GoalEntity) return false

        return this.id == other.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return "GoalEntity(id=$id, userId=$userId, title='$title', description='$description', startDate=$startDate, endDate=$endDate, completed=$completed, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}