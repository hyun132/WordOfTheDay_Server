package com.hyun.demo.dto

data class LearningHistoriesResponse(
    val learningHistories: List<LearningHistoryDTO>,
    val learnedYearMonth: String
)
