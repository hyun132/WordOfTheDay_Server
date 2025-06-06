package com.hyun.demo.auth.entity

import com.hyun.demo.word.entity.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "app_user")
data class AppUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long? = null,

    @Column(unique = true, nullable = false)
    var email: String,

    var password: String,

    ) : BaseEntity()