package com.hyun.demo.word.dto

data class OllamaMessage(
    val role: String,
    val content: String
)

data class OllamaRequest(
    val model: String,
    val messages: List<OllamaMessage>,
    val temperature: Double = 1.2
)

data class OllamaResponse(
    val message: OllamaMessage
)