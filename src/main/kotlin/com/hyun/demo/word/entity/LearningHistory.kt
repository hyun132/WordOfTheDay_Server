package com.hyun.demo.word.entity

import com.hyun.demo.constant.Progress
import jakarta.persistence.*

@Entity
@Table(name = "learning_history")
@Access(AccessType.FIELD)
data class LearningHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_history_id")
    var id: Long? = null,

    val userId: Long,

    var word: String,
    var meaning: String,
    @Enumerated(EnumType.STRING)
    val isDone:Progress = Progress.NOT_STARTED
) : BaseEntity()
