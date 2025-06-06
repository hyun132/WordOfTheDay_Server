package com.hyun.demo.word.entity

import jakarta.persistence.*

@Entity
@Table(name = "bookmark")
@Access(AccessType.FIELD)
data class Bookmark (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    var id: Long? = null,
    val userId: Long,
    val sentence:String
) : BaseEntity()