package com.hyun.demo.service

import com.hyun.demo.constant.Progress
import com.hyun.demo.dto.LearningHistoriesResponse
import com.hyun.demo.dto.LearningHistoryDTO
import com.hyun.demo.dto.WordDTO
import com.hyun.demo.entity.LearningHistory
import com.hyun.demo.repository.LearningHistoryRepository
import com.hyun.demo.util.toDTO
import org.springframework.stereotype.Service
import java.time.LocalTime
import java.time.YearMonth

@Service
class LearningHistoryService(private val learningHistoryRepository: LearningHistoryRepository) {

    fun getAllHistory(userId: Long): List<LearningHistoryDTO> {
        return learningHistoryRepository
            .findAllByUserIdOrderByCreatedDateTimeDesc(userId = userId)
            .map { it.toDTO() }
    }


    fun getAllHistory(userId: Long, yearMonth: String): LearningHistoriesResponse {
        val yearMonthArray = yearMonth.split("-")
        val yearMonthData = YearMonth.of(yearMonthArray[0].toInt(), yearMonthArray[1].toInt())
        val start = yearMonthData.atDay(1).atStartOfDay()
        val end = yearMonthData.atEndOfMonth().atTime(LocalTime.MAX)
        val map = learningHistoryRepository
            .findAllByUserIdAndCreatedDateTimeBetweenOrderByCreatedDateTimeDesc(userId = userId, start, end)
            .map { it.toDTO() }
        return LearningHistoriesResponse(learningHistories = map, yearMonth)
    }

    fun createWordHistory(userId: Long, word: WordDTO): LearningHistoryDTO {
        return learningHistoryRepository.save(
            LearningHistory(
                word = word.word,
                userId = userId
            )
        ).toDTO()
    }

    fun getWordHistoryCount(userId: Long): Long {
        return learningHistoryRepository.countByUserIdAndIsDone(userId = userId, isDone = Progress.COMPLETED)
    }
}