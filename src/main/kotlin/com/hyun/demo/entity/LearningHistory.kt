package com.hyun.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "learning_history")
data class LearningHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_history_id")
    var id: Long? = null,
    val userId: Long,
    val word: String,
) : BaseEntity()
