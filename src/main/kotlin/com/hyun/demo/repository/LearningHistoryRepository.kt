package com.hyun.demo.repository

import com.hyun.demo.entity.LearningHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface LearningHistoryRepository : JpaRepository<LearningHistory, Long> {
    fun findAllByUserIdOrderByCreatedDateTimeDesc(userId: Long): List<LearningHistory>
    fun countByUserId(userId: Long): Long

    fun findByUserIdAndCreatedDateTimeBetween(
        @Param("userId") userId: Long,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): List<LearningHistory>
}