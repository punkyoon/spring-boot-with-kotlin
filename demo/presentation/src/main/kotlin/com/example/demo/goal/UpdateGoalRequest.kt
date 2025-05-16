package com.example.demo.goal

import java.time.LocalDate

data class UpdateGoalRequest(
    val title: String?,
    val description: String?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
)