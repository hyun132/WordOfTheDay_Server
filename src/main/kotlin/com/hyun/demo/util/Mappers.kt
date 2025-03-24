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