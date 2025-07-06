package com.hyun.demo.word.service

import com.hyun.demo.word.dto.SentenceDTO
import com.hyun.demo.word.dto.SentencesDTO
import com.hyun.demo.word.dto.WordDTO
import com.hyun.demo.word.entity.LearningHistory
import com.hyun.demo.word.repository.LearningHistoryRepository
import com.hyun.demo.word.repository.OllamaChatClient
import com.hyun.demo.word.toDTO
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

@Service
class ChatService(
    private val chatClient: OllamaChatClient,
    private val repository: LearningHistoryRepository
) {

    fun plainTextChat(message: String): String {
        return chatClient.plainTextChat(message)
    }

    @Transactional
    fun getWord(userId: Long, subject: String, difficulty: String): WordDTO {
        val word = chatClient.getWord(userId, subject, difficulty)

        val start = LocalDate.now().atStartOfDay()
        val end = LocalDate.now().atTime(LocalTime.MAX)
        val list = repository.findByUserIdAndCreatedDateTimeBetween(userId = userId, startOfDay = start, endOfDay = end)
        if (list.isNotEmpty()) {
            val savedWord = list.first()
            savedWord.word = word.word
        } else {
            repository.save(LearningHistory(userId = userId, word = word.word, meaning = word.meaning))
        }

        return word.toDTO()
    }

    fun getTodaysWord(userId: Long, subject: String, difficulty: String): WordDTO {
        val start = LocalDate.now().atStartOfDay()
        val end = LocalDate.now().atTime(LocalTime.MAX)
        val list = repository.findByUserIdAndCreatedDateTimeBetween(userId = userId, startOfDay = start, endOfDay = end)
        if (list.isNotEmpty()) {
            val savedWord = list.firstOrNull()
            if (savedWord != null) {
                return WordDTO(word = savedWord.word, savedWord.meaning)
            }
        }
        val response = chatClient.getWord(userId, subject, difficulty)
        repository.save(LearningHistory(userId = userId, word = response.word, meaning = response.meaning))

        return response.toDTO()
    }

    fun getSentences(word: String, difficulty: String): SentencesDTO {
        val list = chatClient.getSentences(word, difficulty).map { SentenceDTO(sentence = it) }
        return SentencesDTO(sentences = list)
    }
}