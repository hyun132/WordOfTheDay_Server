package com.hyun.demo.repository

import com.hyun.demo.entity.Bookmark
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookmarkRepository : JpaRepository<Bookmark, Long> {
    fun findAllByUserIdOrderByCreatedDateTimeDesc(userId: Long): List<Bookmark>
    fun countByUserId(userId: Long): Long
}