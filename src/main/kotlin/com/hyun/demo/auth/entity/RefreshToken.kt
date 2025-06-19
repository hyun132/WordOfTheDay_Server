package com.hyun.demo.auth.entity

import com.hyun.demo.word.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
data class RefreshToken(
    @Id
    val userId: Long,
    val expiresAt:LocalDateTime,
    val hashedToken:String
):BaseEntity()