package com.hyun.demo

data class ErrorResponse(
    val message: String,
    val status: Int,
    val errors: Map<String, String>? = null
)