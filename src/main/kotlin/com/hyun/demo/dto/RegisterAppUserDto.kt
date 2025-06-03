package com.hyun.demo.dto

data class RegisterAppUserDto(
    val email: String,
    val username: String,
    val password: String,
    val difficulty: String
)