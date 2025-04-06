package com.hyun.demo.dto.response

data class ErrorResponse(
    val message: String,
    val status: Int,
    val errors: Map<String, String>? = null
)