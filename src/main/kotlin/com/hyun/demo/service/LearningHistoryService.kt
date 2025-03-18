package com.hyun.demo.service

import com.hyun.demo.dto.LearningHistoryDTO
import com.hyun.demo.dto.WordDTO
import com.hyun.demo.entity.LearningHistory
import com.hyun.demo.repository.LearningHistoryRepository
import com.hyun.demo.util.toDTO
import org.springframework.stereotype.Service

@Service
class LearningHistoryService(private val learningHistoryRepository: LearningHistoryRepository) {

    fun getAllHistory(userId: Long): List<LearningHistoryDTO> {
        return learningHistoryRepository
            .findAllByUserIdOrderByCreatedDateTimeDesc(userId = userId)
            .map { it.toDTO() }
    }

    fun createWordHistory(userId: Long, word: WordDTO): LearningHistoryDTO {
        return learningHistoryRepository.save(
            LearningHistory(
                word = word.word,
                userId = userId
            )
        ).toDTO()
    }
}