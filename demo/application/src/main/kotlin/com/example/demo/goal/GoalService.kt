package com.example.demo.goal

import com.example.demo.exception.NotFoundException
import com.example.demo.exception.ValidationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class GoalService(
    private val goalRepository: GoalRepository,
) {

    @Transactional(readOnly = true)
    fun retrieveGoal(userId: Long, goalId: Long): GoalEntity {
        return goalRepository.findByIdAndUserId(goalId, userId)
            ?: throw NotFoundException("Goal `$goalId` not found")
    }

    @Transactional(readOnly = true)
    fun listGoals(userId: Long, filterCompleted: Boolean?, page: Int?, size: Int?): Page<GoalEntity> {
        val pageRequest = PageRequest.of(
            page ?: DEFAULT_PAGE,
            size ?: DEFAULT_SIZE,
            Sort.by(Sort.Direction.DESC, "createdAt"),
        )

        return goalRepository.listPagedGoals(
            userId,
            filterCompleted ?: true,
            pageRequest,
        )
    }

    @Transactional
    fun createNewGoal(
        userId: Long,
        title: String,
        description: String,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): GoalEntity {
        if (startDate != null && endDate != null && startDate > endDate) {
            throw ValidationException("`startDate` must be before than `endDate`")
        }

        val goalEntity = GoalEntity(
            userId = userId,
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate,
        )

        return goalRepository.save(goalEntity)
    }

    @Transactional
    fun updateGoal(
        userId: Long,
        goalId: Long,
        title: String?,
        description: String?,
        startDate: LocalDate?,
        endDate: LocalDate?,
    ): GoalEntity {
        val goalEntity = goalRepository.findByIdAndUserId(goalId, userId)
            ?: throw NotFoundException("Goal `$goalId` not found")

        goalEntity.updateGoal(title, description, startDate, endDate)

        return goalEntity
    }

    @Transactional
    fun checkGoalProgress(userId: Long, goalId: Long) {
        val goal = goalRepository.findByIdAndUserId(goalId, userId)
            ?: throw NotFoundException("Goal `$goalId` not found")

        goal.checkProgress()

        goalRepository.save(goal)
    }

    @Transactional
    fun revertGoalProgress(userId: Long, goalId: Long) {
        val goal = goalRepository.findByIdAndUserId(goalId, userId)
            ?: throw NotFoundException("Goal `$goalId` not found")

        goal.revertProgress()

        goalRepository.save(goal)
    }

    @Transactional
    fun removeGoal(userId: Long, goalId: Long) {
        goalRepository.deleteByIdAndUserId(goalId, userId)
    }

    companion object {
        private const val DEFAULT_PAGE = 1
        private const val DEFAULT_SIZE = 10
    }
}