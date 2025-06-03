package com.hyun.demo.repository

import com.hyun.demo.entity.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository : JpaRepository<AppUser, Long> {
    fun existsByEmail(email:String): Boolean
    fun findByEmail(email: String): AppUser?
}