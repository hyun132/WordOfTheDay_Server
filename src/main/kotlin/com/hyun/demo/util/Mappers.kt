package com.hyun.demo.util

import com.hyun.demo.dto.AppUserDTO
import com.hyun.demo.dto.LearningHistoryDTO
import com.hyun.demo.entity.AppUser
import com.hyun.demo.entity.LearningHistory
import java.time.format.DateTimeFormatter

fun LearningHistory.toDTO(): LearningHistoryDTO {
    return LearningHistoryDTO(
        word = word,
        learnedAt = createdDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
}

fun AppUser.toDto(): AppUserDTO {
    return AppUserDTO(
        username = username,
        id = id ?: -1,
        difficulty = difficulty.name
    )
}