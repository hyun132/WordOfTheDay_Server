package com.hyun.demo.auth.dto.request

data class VerifyCodeRequest(
    val email: String,
    val code: String
)