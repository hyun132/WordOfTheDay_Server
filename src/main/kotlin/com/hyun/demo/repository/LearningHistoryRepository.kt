package com.hyun.demo.repository

import com.hyun.demo.constant.Progress
import com.hyun.demo.entity.LearningHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface LearningHistoryRepository : JpaRepository<LearningHistory, Long> {
    fun findAllByUserIdAndCreatedDateTimeBetweenOrderByCreatedDateTimeDesc(
        userId: Long, startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): List<LearningHistory>

    fun findAllByUserIdOrderByCreatedDateTimeDesc(userId: Long): List<LearningHistory>
    fun countByUserIdAndIsDone(userId: Long, isDone: Progress): Long

    fun findByUserIdAndCreatedDateTimeBetween(
        userId: Long,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): List<LearningHistory>
}