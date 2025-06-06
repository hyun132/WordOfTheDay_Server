package com.hyun.demo.word.repository

import com.hyun.demo.constant.Progress
import com.hyun.demo.word.entity.LearningHistory
import org.springframework.data.jpa.repository.JpaRepository
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