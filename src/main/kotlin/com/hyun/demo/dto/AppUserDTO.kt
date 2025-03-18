package com.hyun.demo.dto

import com.hyun.demo.constant.Difficulty

data class AppUserDTO(
    var id: Long,
    val username: String,
    val difficulty: String
)
