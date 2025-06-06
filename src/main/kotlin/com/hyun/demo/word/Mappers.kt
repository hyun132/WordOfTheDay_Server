package com.hyun.demo.word

import com.hyun.demo.word.dto.BookmarkDTO
import com.hyun.demo.word.dto.LearningHistoryDTO
import com.hyun.demo.word.dto.ProfileDTO
import com.hyun.demo.word.dto.WordDTO
import com.hyun.demo.word.entity.Bookmark
import com.hyun.demo.word.entity.LearningHistory
import com.hyun.demo.word.entity.Profile
import com.hyun.demo.word.entity.Word
import java.time.format.DateTimeFormatter
import java.util.*

fun LearningHistory.toDTO(): LearningHistoryDTO {
    return LearningHistoryDTO(
        word = word,
        learnedAt = createdDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA))
    )
}

fun Profile.toDTO(): ProfileDTO {
    return ProfileDTO(
        username = username,
        difficulty = difficulty,
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