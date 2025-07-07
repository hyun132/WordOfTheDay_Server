package com.hyun.demo.word.repository

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.hyun.demo.word.dto.OllamaMessage
import com.hyun.demo.word.dto.OllamaRequest
import com.hyun.demo.word.entity.Word
import jakarta.transaction.Transactional
import org.springframework.ai.chat.client.ChatClient
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class OllamaChatClient(private val chatClient: ChatClient) {

    fun plainTextChat(message: String): String {
        return chatClient
            .prompt()
            .user(message)
            .call()
            .content() ?: ""
    }

    @Transactional
    fun getWord(userId: Long, subject: String, difficulty: String): Word {
        val randomPromptNoise = listOf("Give", "Show", "Can you give","Please show").random()
        val letter = ('A'..'Z').random()

        val request = OllamaRequest(
            model = "gemma",
            messages = listOf(
                OllamaMessage(
                    role = "user",
                    content = "$randomPromptNoise me one random $subject-related word suitable for $difficulty-level English learners." +
                            "The word must start with the letter '$letter'" +
                            "No explanation. Only a single English word followed by its Korean meaning, without using any markdown, bold, asterisks, or quotation marks. For example:'Train - 기차'. Choose differently every time."
                )
            ),
            temperature = 1.2
        )

        val restTemplate = RestTemplate().apply {
            messageConverters.add(0, StringHttpMessageConverter(Charsets.UTF_8))
        }

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

        println(responseStr)

        val objectMapper = jacksonObjectMapper()
        val fullWord = responseStr
            .lineSequence()
            .mapNotNull { line ->
                try {
                    val node = objectMapper.readTree(line)
                    node["message"]?.get("content")?.asText()
                } catch (e: Exception) {
                    null
                }
            }
            .joinToString("") // content 문자열을 이어붙임

        val splited = fullWord.split(" - ")
        println(fullWord)
        return Word(word = splited[0].trim(), meaning = splited[1].trim())
    }

    fun getTodaysWord(userId: Long, subject: String, difficulty: String): Word {

        val response = chatClient
            .prompt()
            .user("Suggest new word about $subject field for $difficulty, excluding duplicates. Without any modifiers or explanations. Just one word in dictionary.")
            .call()
            .content() ?: ""

        return Word(word = response, "")
    }

    fun getSentences(word: String, meaning: String, difficulty: String): List<String> {
        val prompt = """
Return 5 useful English sentences using the word '$word' for $difficulty learners.
However, the sentence must be one in which the word word is used in the sense of '$meaning'.
Respond only with a valid JSON like below:

[
  "First sentence.",
  "Second sentence.",
  "Third sentence.",
  "Fourth sentence.",
  "Fifth sentence."
]
"""

        val raw = chatClient
            .prompt()
            .user(prompt)
            .call()
            .content()

        val fixedRaw = raw?.substringAfter("[")?.let { "[" + it } ?: "[]"
        val mapper = jacksonObjectMapper()
        val result: List<String> = try {
            mapper.readValue(fixedRaw ?: "")
        } catch (e: Exception) {
            emptyList()
        }
        return result
    }
}