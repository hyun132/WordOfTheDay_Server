package com.hyun.demo.auth.dto.request

data class PasswordUpdateRequest(
    val eamil: String,
    val password: String,
)
