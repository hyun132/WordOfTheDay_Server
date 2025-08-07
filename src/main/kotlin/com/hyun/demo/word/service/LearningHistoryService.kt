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
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Service
class LearningHistoryService(private val learningHistoryRepository: LearningHistoryRepository) {

    companion object {
        private val YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM")
    }

    @Transactional(readOnly = true)
    fun getAllCompletedHistory(userId: Long): List<LearningHistoryDTO> {
        return learningHistoryRepository
            .findAllByUserIdOrderByCreatedDateTimeDesc(userId = userId)
            .map { it.toDTO() }
    }

    @Transactional(readOnly = true)
    fun getAllCompletedHistory(userId: Long, yearMonth: String): LearningHistoriesResponse {
        val dateRange = getMonthDateRange(yearMonth)
        val histories = learningHistoryRepository
            .findAllByUserIdAndIsDoneAndCreatedDateTimeBetweenOrderByCreatedDateTimeDesc(
                userId = userId,
                isDone = Progress.COMPLETED,
                dateRange.first,
                dateRange.second,
            )
            .map { it.toDTO() }
        return LearningHistoriesResponse(learningHistories = histories, yearMonth)
    }

    @Transactional
    fun createWordHistory(userId: Long, word: WordDTO): LearningHistoryDTO {
        var existingHistories = learningHistoryRepository.findByUserIdAndWordAndMeaningOrderByCreatedDateTimeDesc(
            userId,
            word = word.word,
            meaning = word.meaning
        )
        if (existingHistories.isEmpty()) {
            return createNewHistory(userId, word).toDTO()
        }

        var lastWord = existingHistories.first()
        return if (shouldUpdateExistingHistory(lastWord, word)) {
            updateExistingHistory(lastWord, word).toDTO()
        } else createNewHistory(userId, word).toDTO()
    }

    @Transactional(readOnly = true)
    fun getWordHistoryCount(userId: Long): Long {
        return learningHistoryRepository.countByUserIdAndIsDone(userId = userId, isDone = Progress.COMPLETED)
    }

    @Transactional(readOnly = true)
    fun getLongestStreakDays(userId: Long): Int {
        return learningHistoryRepository.findLongestLearningStreak(userId) ?: 0
    }

    @Transactional(readOnly = true)
    fun getCurrentStreakDays(userId: Long): Int {
        return learningHistoryRepository.findCurrentLearningStreak(userId) ?: 0
    }

    fun getMonthDateRange(yearMonth: String): Pair<LocalDateTime, LocalDateTime> {
        return try {
            val yearMonthData = YearMonth.parse(yearMonth, YEAR_MONTH_FORMATTER)
            Pair(
                yearMonthData.atDay(1).atStartOfDay(),
                yearMonthData.atEndOfMonth().atTime(LocalTime.MAX)
            )
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid yearMonth format. Expected format: yyyy-MM")
        }
    }


    fun updateExistingHistory(history: LearningHistory, word: WordDTO): LearningHistory {
        val update = history.copy(isDone = Progress.COMPLETED)
        update.createdDateTime = history.createdDateTime

        return learningHistoryRepository.save(update)
    }

    fun createNewHistory(userId: Long, word: WordDTO): LearningHistory {
        return learningHistoryRepository.save(
            LearningHistory(
                word = word.word,
                meaning = word.meaning,
                userId = userId,
                isDone = Progress.NOT_STARTED
            )
        )
    }

    fun shouldUpdateExistingHistory(history: LearningHistory, word: WordDTO): Boolean {
        return history.word == word.word && history.meaning == word.meaning && history.isDone != Progress.COMPLETED
    }

}