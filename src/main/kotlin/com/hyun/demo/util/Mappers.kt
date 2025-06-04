package com.hyun.demo.util

import com.hyun.demo.dto.AppUserDTO
import com.hyun.demo.dto.BookmarkDTO
import com.hyun.demo.dto.LearningHistoryDTO
import com.hyun.demo.dto.WordDTO
import com.hyun.demo.entity.AppUser
import com.hyun.demo.entity.Bookmark
import com.hyun.demo.entity.LearningHistory
import com.hyun.demo.entity.Word
import java.time.format.DateTimeFormatter
import java.util.*

fun LearningHistory.toDTO(): LearningHistoryDTO {
    return LearningHistoryDTO(
        word = word,
        learnedAt = createdDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA))
    )
}

fun AppUser.toDTO(): AppUserDTO {
    return AppUserDTO(
        username = username,
        email = email,
        difficulty = difficulty.name,
        createdAt = createdDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        topic = topic
    )
}

fun Word.toDTO(): WordDTO {
    return WordDTO(
        word = word
    )
}

fun Bookmark.toDTO(): BookmarkDTO {
    return BookmarkDTO(
        sentence = sentence
    )
}