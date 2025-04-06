package com.hyun.demo.repository

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class OllamaClient(
    private val webClient: WebClient
) {

    fun getEmbedding(text: String): List<Float> {
        val request = mapOf("model" to "nomic-embed-text", "prompt" to text)

        val response = webClient.post()
            .uri("/api/embeddings")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OllamaEmbeddingResponse::class.java)
            .block() ?: error("Ollama 응답 없음")

        return response.embedding
    }
}

data class OllamaEmbeddingResponse(
    val embedding: List<Float>
)