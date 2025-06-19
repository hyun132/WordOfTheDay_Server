package com.hyun.demo.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    val email:String,
    @field:NotBlank
    val password:String
)
