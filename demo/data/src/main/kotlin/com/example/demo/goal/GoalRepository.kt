package com.example.demo.goal

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface GoalRepository : JpaRepository<GoalEntity, Long> {

    fun findByIdAndUserId(id: Long, userId: Long): GoalEntity?

    fun findAllByUserId(userId: Long): List<GoalEntity>

    @Query(
        """
        SELECT g
        FROM GoalEntity g
        WHERE g.userId = :userId
        AND (:filterCompleted = false OR g.completed != true)
        """
    )
    fun listPagedGoals(
        userId: Long,
        filterCompleted: Boolean,
        pageable: Pageable,
    ): Page<GoalEntity>

    fun deleteByIdAndUserId(id: Long, userId: Long)
}