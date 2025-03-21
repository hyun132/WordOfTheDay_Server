package com.hyun.demo.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hyun.demo.dto.OllamaMessage
import com.hyun.demo.dto.OllamaRequest
import com.hyun.demo.entity.Word
import jakarta.transaction.Transactional
import org.springframework.ai.chat.client.ChatClient
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class ChatRepository(private val chatClient: ChatClient) {

    fun plainTextChat(message: String): String {
        return chatClient
            .prompt()
            .user(message)
            .call()
            .content() ?: ""
    }

    @Transactional
    fun getWord(userId: Long, subject: String, difficulty: String): Word {

        val request = OllamaRequest(
            model = "llama3.2",
            messages = listOf(
                OllamaMessage(
                    role = "user",
                    content = "Give me another word about $subject for $difficulty students excluding duplicates. Without any modifiers or explanations. Just one word."
                )
            ),
            temperature = 1.2
        )

        val restTemplate = RestTemplate()

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.ALL) // x-ndjson 허용
        }

        val entity = HttpEntity(request, headers)

        val responseStr = restTemplate.exchange(
            "http://localhost:11434/api/chat",
            HttpMethod.POST,
            entity,
            String::class.java
        ).body ?: ""

        val firstLine = responseStr.lineSequence().firstOrNull {
            it.contains("message")
        } ?: ""

        val objectMapper = jacksonObjectMapper()
        val jsonNode = objectMapper.readTree(firstLine)
        val word = jsonNode["message"]?.get("content")?.asText() ?: ""

        return Word(word = word)
    }

    fun getTodaysWord(userId: Long, subject: String, difficulty: String): Word {

        val response = chatClient
            .prompt()
            .user("Suggest new word about $subject field for $difficulty, excluding duplicates. Without any modifiers or explanations. Just one word.")
            .call()
            .content() ?: ""

        return Word(word = response)
    }

    fun getSentences(word: String, difficulty: String): List<String> {
        val answer = chatClient
            .prompt()
            .user("Show me 5 useful sample sentences using $word for $difficulty. Just Numbered sentences only.")
            .call()
            .content()


        val list = answer?.split(",")?.filter { it.matches(Regex("^\\d+\\..*")) }?.toList() ?: emptyList()

        return list
    }

}