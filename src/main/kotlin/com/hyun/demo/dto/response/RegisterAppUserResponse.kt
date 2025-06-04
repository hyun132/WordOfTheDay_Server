package com.hyun.demo.dto.response

import com.hyun.demo.constant.Difficulty

data class RegisterAppUserResponse(
    val email: String,
    val username: String,
    val topic: String,
    val difficulty: Difficulty,
    val createdAt: String,
)