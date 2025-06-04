package com.hyun.demo.entity

import com.hyun.demo.constant.Difficulty
import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@Table(name = "app_user")
data class AppUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var email: String,

    var username: String,

    var password: String,

    var topic: String,

    @Enumerated(EnumType.STRING) // ENUM을 문자열로 저장
    @Column(nullable = false)
    var difficulty: Difficulty = Difficulty.BEGINNER
) : BaseEntity()