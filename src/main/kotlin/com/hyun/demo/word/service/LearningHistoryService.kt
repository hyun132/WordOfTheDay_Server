package com.hyun.demo.word.service

import com.hyun.demo.constant.Progress
import com.hyun.demo.word.dto.LearningHistoryDTO
import com.hyun.demo.word.dto.WordDTO
import com.hyun.demo.word.dto.response.LearningHistoriesResponse
import com.hyun.demo.word.entity.LearningHistory
import com.hyun.demo.word.repository.LearningHistoryRepository
import com.hyun.demo.word.toDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.time.YearMonth

@Service
class LearningHistoryService(private val learningHistoryRepository: LearningHistoryRepository) {

    fun getAllCompletedHistory(userId: Long): List<LearningHistoryDTO> {
        return learningHistoryRepository
            .findAllByUserIdOrderByCreatedDateTimeDesc(userId = userId)
            .map { it.toDTO() }
    }


    fun getAllCompletedHistory(userId: Long, yearMonth: String): LearningHistoriesResponse {
        val yearMonthArray = yearMonth.split("-")
        val yearMonthData = YearMonth.of(yearMonthArray[0].toInt(), yearMonthArray[1].toInt())
        val start = yearMonthData.atDay(1).atStartOfDay()
        val end = yearMonthData.atEndOfMonth().atTime(LocalTime.MAX)
        val map = learningHistoryRepository
            .findAllByUserIdAndIsDoneAndCreatedDateTimeBetweenOrderByCreatedDateTimeDesc(
                userId = userId,
                isDone = Progress.COMPLETED,
                start,
                end,
            )
            .map { it.toDTO() }
        return LearningHistoriesResponse(learningHistories = map, yearMonth)
    }

    @Transactional
    fun createWordHistory(userId: Long, word: WordDTO): LearningHistoryDTO {
        var saved = learningHistoryRepository.findByUserIdAndWordAndMeaningOrderByCreatedDateTimeDesc(
            userId,
            word = word.word,
            meaning = word.meaning
        )
        var lastWord = saved[0]
        if (lastWord.word == word.word && lastWord.meaning == word.meaning && lastWord.isDone != Progress.COMPLETED) {
            val update = lastWord.copy(isDone = Progress.COMPLETED)
            update.createdDateTime = lastWord.createdDateTime

            return learningHistoryRepository.save(update).toDTO()
        }

        return learningHistoryRepository.save(
            LearningHistory(
                word = word.word,
                meaning = word.meaning,
                userId = userId,
                isDone = Progress.COMPLETED
            )
        ).toDTO()
    }

    fun getWordHistoryCount(userId: Long): Long {
        return learningHistoryRepository.countByUserIdAndIsDone(userId = userId, isDone = Progress.COMPLETED)
    }

    fun getLongestStreakDays(userId: Long): Int {
        return learningHistoryRepository.findLongestLearningStreak(userId) ?: 0
    }

    fun getCurrentStreakDays(userId: Long): Int {
        return learningHistoryRepository.findCurrentLearningStreak(userId) ?: 0
    }
}