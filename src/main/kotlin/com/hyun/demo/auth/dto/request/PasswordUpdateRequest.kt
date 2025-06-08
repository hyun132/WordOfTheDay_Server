package com.hyun.demo.auth.dto.request

data class PasswordUpdateRequest(
    val currentPassword: String,
    val newPassword: String
)
