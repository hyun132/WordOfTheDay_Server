package com.hyun.demo.auth.dto.response

data class TokenPair(
    val accessToken:String,
    val refreshToken:String,
)
