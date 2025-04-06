package com.hyun.demo.service

import com.hyun.demo.dto.request.AnswerSubmitRequest
import com.hyun.demo.dto.response.AnswerCheckResponse
import com.hyun.demo.repository.OllamaClient
import org.springframework.stereotype.Service
import kotlin.math.sqrt

@Service
class LearningService(
    val ollamaClient: OllamaClient
) {

    fun checkAnswer(request: AnswerSubmitRequest): AnswerCheckResponse {
        val correctEmbedding = ollamaClient.getEmbedding(request.origin)
        val userEmbedding = ollamaClient.getEmbedding(request.userAnswer)

        val similarity = cosineSimilarity(correctEmbedding, userEmbedding)
        val isCorrect = similarity > 0.9f

        return AnswerCheckResponse(
            isCorrect = isCorrect,
            userInput = request.userAnswer,
            mode = request.mode,
            similarity = similarity,
            correctAnswer = request.origin,
            feedback = ""
        )
    }

    fun cosineSimilarity(a: List<Float>, b: List<Float>): Float {
        val dotProduct = a.zip(b).sumOf { (x, y) -> (x * y).toDouble() }.toFloat()
        val magnitudeA = sqrt(a.sumOf { (it * it).toDouble() }.toFloat())
        val magnitudeB = sqrt(b.sumOf { (it * it).toDouble() }.toFloat())

        return if (magnitudeA != 0f && magnitudeB != 0f) {
            dotProduct / (magnitudeA * magnitudeB)
        } else 0f
    }
}