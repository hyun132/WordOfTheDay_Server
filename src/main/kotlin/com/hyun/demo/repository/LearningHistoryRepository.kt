package com.hyun.demo.repository

import com.hyun.demo.entity.LearningHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LearningHistoryRepository : JpaRepository<LearningHistory, Long> {
    fun findAllByUserIdOrderByCreatedDateTimeDesc(userId: Long): List<LearningHistory>
    fun countByUserId(userId: Long): Long
}