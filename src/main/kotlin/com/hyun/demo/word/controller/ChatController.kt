package com.hyun.demo.word.controller

import com.hyun.demo.word.dto.SentencesDTO
import com.hyun.demo.word.dto.WordDTO
import com.hyun.demo.word.service.ChatService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ChatController(
    private val service: ChatService
) {

    @GetMapping("/chat")
    fun chat(@RequestParam("message") message: String): String? {
        return service.plainTextChat(message = message)
    }

    @GetMapping("/word")
    fun getWord(
        @RequestParam("subject") subject: String,
        @RequestParam("difficulty") difficulty: String
    ): ResponseEntity<WordDTO>? {
        val userId = getUserIdFromContext()
        return ResponseEntity.ok(service.getTodaysWord(userId, subject, difficulty))
    }

    @GetMapping("/word/new")
    fun getNewWord(
        @RequestParam("subject") subject: String,
        @RequestParam("difficulty") difficulty: String
    ): ResponseEntity<WordDTO>? {
        val userId = getUserIdFromContext()
        return ResponseEntity.ok(service.getWord(userId, subject, difficulty))
    }

    @GetMapping("/sentence")
    fun getSentences(
        @RequestParam("word") word: String,
        @RequestParam("difficulty") difficulty: String
    ): ResponseEntity<SentencesDTO> {
        return ResponseEntity.ok(service.getSentences(word, difficulty))
    }

    fun getUserIdFromContext(): Long {
        return (SecurityContextHolder.getContext().authentication.principal as String).toLong()
    }
}