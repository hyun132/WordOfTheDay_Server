package com.hyun.demo.service

import com.hyun.demo.dto.BookmarkDTO
import com.hyun.demo.dto.BookmarksDTO
import com.hyun.demo.entity.Bookmark
import com.hyun.demo.repository.BookmarkRepository
import com.hyun.demo.util.toDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookmarkService(private val bookmarkRepository: BookmarkRepository) {

    fun getAllBookMarks(userId: Long): BookmarksDTO {
        val list = bookmarkRepository
            .findAllByUserIdOrderByCreatedDateTimeDesc(userId = userId)
            .map { it.toDTO() }
        return BookmarksDTO(sentences = list)
    }

    fun createBookMark(userId: Long, bookMark: BookmarkDTO): BookmarkDTO {
        return bookmarkRepository.save(
            Bookmark(
                sentence = bookMark.sentence,
                userId = userId
            )
        ).toDTO()
    }

    @Transactional
    fun deleteBookMark(userId: Long, bookMark: BookmarkDTO): BookmarkDTO {
        bookmarkRepository.deleteBookmarkByUserIdAndSentence(
            sentence = bookMark.sentence,
            userId = userId
        )
        return bookMark
    }
}