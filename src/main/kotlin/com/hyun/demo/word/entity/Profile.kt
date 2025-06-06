package com.hyun.demo.word.entity

import com.hyun.demo.constant.Difficulty
import jakarta.persistence.*

@Entity
@Table(name = "profile")
data class Profile(
    @Id
    var id: Long,

    var username: String,

    var topic: String,

    @Enumerated(EnumType.STRING) // ENUM을 문자열로 저장
    @Column(nullable = false)
    var difficulty: Difficulty = Difficulty.BEGINNER
) : BaseEntity()