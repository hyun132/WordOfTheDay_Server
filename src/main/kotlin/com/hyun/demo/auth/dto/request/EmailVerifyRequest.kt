package com.hyun.demo.auth.dto.request

data class EmailVerifyRequest(
    val email: String,
    val code: String
)