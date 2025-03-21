package com.hyun.demo.entity

import com.hyun.demo.constant.Progress
import jakarta.persistence.*

@Entity
@Table(name = "learning_history")
data class LearningHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_history_id")
    var id: Long? = null,

    val userId: Long,

    var word: String,

    @Enumerated(EnumType.STRING)
    val isDone:Progress = Progress.NOT_STARTED
) : BaseEntity()
