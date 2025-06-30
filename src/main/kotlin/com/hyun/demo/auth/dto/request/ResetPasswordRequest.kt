package com.hyun.demo.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class ResetPasswordRequest(
    @field:Email
    val email: String,
    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val newPassword: String
)
