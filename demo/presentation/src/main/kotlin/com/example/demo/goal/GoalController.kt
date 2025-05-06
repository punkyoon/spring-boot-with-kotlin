package com.example.demo.goal

import com.example.demo.common.AuthContext
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
        authContext: AuthContext,
        @PathVariable("goalId") goalId: Long,
    ): GoalResponse {
        return goalService.retrieveGoal(authContext.userId, goalId).toGoalResponse()
    }

    @GetMapping("/goals/list")
    fun listGoals(
        authContext: AuthContext,
        @RequestParam("filterCompleted", defaultValue = "true", required = false) filterCompleted: Boolean?,
        @RequestParam("page", defaultValue = "1", required = false) page: Int?,
        @RequestParam("size", defaultValue = "10", required = false) size: Int?,
    ): PageResponse<GoalResponse> {
        val pagedGoalEntities = goalService.listGoals(authContext.userId, filterCompleted, page, size)

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
        authContext: AuthContext,
        @RequestBody request: CreateGoalRequest,
    ): GoalResponse {
        val goalEntity = goalService.createNewGoal(
            userId = authContext.userId,
            title = request.title,
            description = request.description,
            startDate = request.startDate,
            endDate = request.endDate,
        )

        return goalEntity.toGoalResponse()
    }

    @PatchMapping("/goals/{goalId}")
    fun updateGoal(
        authContext: AuthContext,
        @PathVariable("goalId") goalId: Long,
        @RequestBody request: UpdateGoalRequest,
    ): GoalResponse {
        val goalEntity = goalService.updateGoal(
            userId = authContext.userId,
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
        authContext: AuthContext,
        @PathVariable("goalId") goalId: Long,
    ) {
        goalService.checkGoalProgress(authContext.userId, goalId)
    }

    @PostMapping("/goals/{goalId}/revert")
    fun revertGoalProgress(
        authContext: AuthContext,
        @PathVariable("goalId") goalId: Long,
    ) {
        goalService.revertGoalProgress(authContext.userId, goalId)
    }

    @DeleteMapping("/goals/{goalId}")
    fun removeGoal(
        authContext: AuthContext,
        @PathVariable("goalId") goalId: Long,
    ) {
        goalService.removeGoal(authContext.userId, goalId)
    }
}