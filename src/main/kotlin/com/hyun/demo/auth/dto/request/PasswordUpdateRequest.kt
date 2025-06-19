package com.hyun.demo.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class PasswordUpdateRequest(
    val currentPassword: String,
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val newPassword: String
)
