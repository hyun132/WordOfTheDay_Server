package com.hyun.demo.word.dto.response

import com.hyun.demo.word.dto.LearningHistoryDTO

data class LearningHistoriesResponse(
    val learningHistories: List<LearningHistoryDTO>,
    val learnedYearMonth: String
)
