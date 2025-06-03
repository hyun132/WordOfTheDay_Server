package com.hyun.demo.service

import com.hyun.demo.dto.SentenceDTO
import com.hyun.demo.dto.SentencesDTO
import com.hyun.demo.dto.WordDTO
import com.hyun.demo.entity.LearningHistory
import com.hyun.demo.repository.OllamaChatClient
import com.hyun.demo.repository.LearningHistoryRepository
import com.hyun.demo.util.toDTO
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
//            repository.save(LearningHistory(userId = userId, word = word.word))
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
                return WordDTO(word = savedWord.word)
            }
        }
        val response = chatClient.getWord(userId, subject, difficulty)
//        repository.save(LearningHistory(userId = userId, word = response.word))

        return response.toDTO()
    }

    fun getSentences(word: String, difficulty: String): SentencesDTO {
        val list = chatClient.getSentences(word, difficulty).map { SentenceDTO(sentence = it) }
        return SentencesDTO(sentences = list)
    }
}