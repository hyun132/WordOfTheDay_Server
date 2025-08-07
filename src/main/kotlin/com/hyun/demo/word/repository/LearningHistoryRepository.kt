package com.hyun.demo.word.repository

import com.hyun.demo.constant.Progress
import com.hyun.demo.word.entity.LearningHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface LearningHistoryRepository : JpaRepository<LearningHistory, Long> {
    fun findAllByUserIdAndIsDoneAndCreatedDateTimeBetweenOrderByCreatedDateTimeDesc(
        userId: Long,
        isDone: Progress,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime,
    ): List<LearningHistory>

    fun findAllByUserIdOrderByCreatedDateTimeDesc(userId: Long): List<LearningHistory>

    fun findByUserIdAndWordAndMeaningOrderByCreatedDateTimeDesc(
        userId: Long,
        word: String,
        meaning: String
    ): List<LearningHistory>

    fun countByUserIdAndIsDone(userId: Long, isDone: Progress): Long

    fun findByUserIdAndCreatedDateTimeBetween(
        userId: Long,
        startOfDay: LocalDateTime,
        endOfDay: LocalDateTime
    ): List<LearningHistory>

    /**
     * 사용자의 최장 연속 학습 일수
     */
    @Query("""
        WITH ConsecutiveDays AS (
            SELECT 
                created_date,
                user_id,
                DATE_SUB(created_date, INTERVAL ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY created_date) DAY) as grp
            FROM (
                SELECT DISTINCT DATE(created_date_time) as created_date, user_id
                FROM learning_history
                WHERE user_id = :userId AND is_done = 'COMPLETED'
            ) t
        )
        SELECT COUNT(*) as streak_length
        FROM ConsecutiveDays
        GROUP BY user_id, grp
        ORDER BY streak_length DESC
        LIMIT 1
    """, nativeQuery = true)
    fun findLongestLearningStreak(userId: Long): Int?

    /**
     * 사용자의 현재 연속 학습 일수
     */
    @Query("""
        WITH ConsecutiveDays AS (
            SELECT 
                created_date,
                user_id,
                DATE_SUB(created_date, INTERVAL ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY created_date DESC) DAY) as grp
            FROM (
                SELECT DISTINCT DATE(created_date_time) as created_date, user_id
                FROM learning_history
                WHERE user_id = :userId AND is_done = 'COMPLETED'
            ) t
        )
        SELECT COUNT(*) as current_streak
        FROM ConsecutiveDays
        WHERE created_date <= CURDATE()
        GROUP BY user_id, grp
        HAVING MAX(created_date) = CURDATE()
        LIMIT 1
    """, nativeQuery = true)
    fun findCurrentLearningStreak(userId: Long): Int?
}