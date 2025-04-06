package com.hyun.demo.dto.response

import com.hyun.demo.constant.Mode

data class AnswerCheckResponse(
    val isCorrect: Boolean,
    val similarity: Float,
    val correctAnswer: String,
    val userInput: String,
    val mode: Mode,
    val feedback: String
)
