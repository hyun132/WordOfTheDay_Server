package com.hyun.demo.word.dto

import com.hyun.demo.constant.Difficulty

data class ProfileDTO(
    var username: String,
    var topic: String,
    var difficulty: Difficulty = Difficulty.BEGINNER
)
