package com.example.demo.goal

import com.example.demo.exception.NotFoundException
import com.example.demo.exception.ValidationException
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

//  this extension will call `unmockkAll` and `clearAllMocks` in a `@AfterAll` callback,
//  ensuring your test environment is clean after each test class execution
@ExtendWith(MockKExtension::class)
class GoalServiceTest {

    private val goalRepository = mockk<GoalRepository>()

    private val goalService = GoalService(
        goalRepository = goalRepository,
    )

    @Test
    fun `test retrieveGoal`() {
        val userId = 1L

        every { goalRepository.findByIdAndUserId(1L, userId) } returns GoalEntity(
            userId = userId,
            title = "test",
            description = "test",
            startDate = null,
            endDate = null
        )
        every { goalRepository.findByIdAndUserId(2L, userId) } returns null

        assertDoesNotThrow {
            goalService.retrieveGoal(userId, 1L)
        }

        assertThrows<NotFoundException> {
            goalService.retrieveGoal(userId, 2L)
        }
    }

    @Test
    fun `test createGoal`() {
        val userId = 1L
        val title = "Learn Kotlin"
        val description = "Complete Kotlin course"
        val startDate = LocalDate.of(2025, 1, 1)
        val endDate = LocalDate.of(2025, 12, 31)

        val goalEntity = GoalEntity(
            userId = userId,
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate
        )

        every { goalRepository.save(any()) } returns goalEntity

        val result = goalService.createNewGoal(userId, title, description, startDate, endDate)

        verify(exactly = 1) { goalRepository.save(any()) }

        assertEquals(title, result.title)
        assertEquals(description, result.description)
        assertEquals(startDate, result.startDate)
        assertEquals(endDate, result.endDate)
        assert(!result.completed)

        assertThrows<ValidationException> {
            goalService.createNewGoal(
                userId = userId,
                title = title,
                description = description,
                startDate = LocalDate.of(2025, 5, 5),
                endDate = LocalDate.of(2025, 5 ,1),
            )
        }
    }

    @Test
    fun `test updateGoal`() {
        val userId = 1L
        val goalId = 1L

        val title = "Learn Kotlin"
        val description = "Complete Kotlin course"
        val startDate = null
        val endDate = null

        val goalEntity = GoalEntity(
            userId = userId,
            title = title,
            description = description,
            startDate = startDate,
            endDate = endDate,
        )

        every { goalRepository.findByIdAndUserId(any(), any()) } returns goalEntity

        assertThrows<ValidationException> {
            goalService.updateGoal(
                userId = userId,
                goalId = goalId,
                title = null,
                description = null,
                startDate = LocalDate.of(2025, 5, 5),
                endDate = LocalDate.of(2025, 5, 1),
            )
        }

        val newTitle = "Learn Kotlin + Spring Boot"
        val updatedGoalEntity = goalService.updateGoal(
            userId = userId,
            goalId = goalId,
            title = newTitle,
            description = null,
            startDate = null,
            endDate = null,
        )

        assertEquals(newTitle, updatedGoalEntity.title)
        assertEquals(description, updatedGoalEntity.description)
        assertEquals(startDate, updatedGoalEntity.startDate)
        assertEquals(endDate, updatedGoalEntity.endDate)
    }

    @Test
    fun `test checkGoalProgress + revertGoalProgress`() {
        val userId = 1L
        val goalId = 1L
        val goalEntity = GoalEntity(
            userId = userId,
            title = "Learn Kotlin",
            description = "Learn Kotlin with Spring Boot"
        )
        val goalProgressEntity = GoalProgressEntity(goal = goalEntity)

        updateProperty(goalEntity, "id", goalId)
        updateProperty(goalEntity, "progress", goalProgressEntity)

        every { goalRepository.findByIdAndUserId(goalId, userId) } returns goalEntity
        every { goalRepository.save(any()) } returns goalEntity

        val repeatTimes = 99
        repeat(repeatTimes) {
            goalService.checkGoalProgress(userId, goalId)
        }

        verify { goalRepository.findByIdAndUserId(goalId, userId) }
        verify { goalRepository.save(goalEntity) }

        assertEquals(repeatTimes, goalEntity.progress!!.progressRate)
        assert(!goalEntity.completed)

        goalService.checkGoalProgress(userId, goalId)

        assertEquals(repeatTimes + 1, goalEntity.progress!!.progressRate)
        assert(goalEntity.completed)

        goalService.revertGoalProgress(userId, goalId)

        assertEquals(repeatTimes, goalEntity.progress!!.progressRate)
        assert(!goalEntity.completed)
    }

    // Use reflection
    private fun updateProperty(targetData: Any, propertyName: String, propertyValue: Any?) {
        val property = targetData::class.java.getDeclaredField(propertyName)

        property.isAccessible = true
        property.set(targetData, propertyValue)
    }
}