package com.example.demo.goal

import java.time.LocalDate

data class GoalResponse(
    val goalId: Long,
    val title: String,
    val description: String,
    val completed: Boolean,
    val progressRate: Int,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
)

fun GoalEntity.toGoalResponse(): GoalResponse {
    return GoalResponse(
        goalId = this.id!!,
        title = this.title,
        description = this.description,
        completed = this.completed,
        progressRate = this.progress?.progressRate ?: 0,
        startDate = this.startDate,
        endDate = this.endDate,
    )
}