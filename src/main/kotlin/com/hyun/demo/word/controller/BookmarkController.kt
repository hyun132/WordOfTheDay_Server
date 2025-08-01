package com.hyun.demo.word.controller

import com.hyun.demo.word.dto.BookmarkDTO
import com.hyun.demo.word.dto.BookmarksDTO
import com.hyun.demo.word.service.BookmarkService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/me/bookmark")
class BookmarkController(
    private val bookmarkService: BookmarkService
) {

    @PostMapping
    fun createBookmark(@RequestBody bookmarkDTO: BookmarkDTO): ResponseEntity<BookmarkDTO> {
        val userId = getUserIdFromContext()

        val result = bookmarkService.createBookMark(userId = userId, bookMark = bookmarkDTO)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun getBookmarks(): ResponseEntity<BookmarksDTO> {
        val userId = getUserIdFromContext()

        val list = bookmarkService.getAllBookMarks(userId = userId)

        return ResponseEntity.ok(list)
    }

    @DeleteMapping
    fun deleteBookmarks(@RequestBody bookmarkDTO: BookmarkDTO): ResponseEntity<BookmarkDTO> {
        val userId = getUserIdFromContext()

        val list = bookmarkService.deleteBookMark(userId = userId, bookMark = bookmarkDTO)

        return ResponseEntity.ok(list)
    }

    fun getUserIdFromContext(): Long {
        return (SecurityContextHolder.getContext().authentication.principal as String).toLong()
    }
}