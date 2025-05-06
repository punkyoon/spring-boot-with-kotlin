package com.example.demo.goal

import com.example.demo.common.PageResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GoalController(
    private val goalService: GoalService,
) {

    @GetMapping("/goals/{goalId}")
    fun retrieveGoal(
        @PathVariable("goalId") goalId: Long,
    ): GoalResponse {
        return goalService.retrieveGoal(TEMP_USER_ID, goalId).toGoalResponse()
    }

    @GetMapping("/goals/list")
    fun listGoals(
        @RequestParam("filterCompleted", defaultValue = "true", required = false) filterCompleted: Boolean?,
        @RequestParam("page", defaultValue = "1", required = false) page: Int?,
        @RequestParam("size", defaultValue = "10", required = false) size: Int?,
    ): PageResponse<GoalResponse> {
        val pagedGoalEntities = goalService.listGoals(TEMP_USER_ID, filterCompleted, page, size)

        return PageResponse(
            items = pagedGoalEntities.content.map { it.toGoalResponse() },
            page = pagedGoalEntities.pageable.pageNumber,
            lastPage = pagedGoalEntities.isLast,
            totalPage = pagedGoalEntities.totalPages,
            totalItems = pagedGoalEntities.totalElements,
        )
    }

    @PostMapping("/goals")
    fun createGoal(
        @RequestBody request: CreateGoalRequest,
    ): GoalResponse {
        val goalEntity = goalService.createNewGoal(
            userId = TEMP_USER_ID,
            title = request.title,
            description = request.description,
            startDate = request.startDate,
            endDate = request.endDate,
        )

        return goalEntity.toGoalResponse()
    }

    @PatchMapping("/goals/{goalId}")
    fun updateGoal(
        @PathVariable("goalId") goalId: Long,
        @RequestBody request: UpdateGoalRequest,
    ): GoalResponse {
        val goalEntity = goalService.updateGoal(
            userId = TEMP_USER_ID,
            goalId = goalId,
            title = request.title,
            description = request.description,
            startDate = request.startDate,
            endDate = request.endDate,
        )

        return goalEntity.toGoalResponse()
    }

    @PostMapping("/goals/{goalId}/check")
    fun checkGoalProgress(
        @PathVariable("goalId") goalId: Long,
    ) {
        goalService.checkGoalProgress(TEMP_USER_ID, goalId)
    }

    @PostMapping("/goals/{goalId}/revert")
    fun revertGoalProgress(
        @PathVariable("goalId") goalId: Long,
    ) {
        goalService.revertGoalProgress(TEMP_USER_ID, goalId)
    }

    @DeleteMapping("/goals/{goalId}")
    fun removeGoal(
        @PathVariable("goalId") goalId: Long,
    ) {
        goalService.removeGoal(TEMP_USER_ID, goalId)
    }

    companion object {
        private const val TEMP_USER_ID = 1L
    }
}