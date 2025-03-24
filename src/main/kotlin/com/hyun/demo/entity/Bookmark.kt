package com.hyun.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "bookmark")
data class Bookmark (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    var id: Long? = null,
    val userId: Long,
    val sentence:String
) : BaseEntity()