package com.hyun.demo.controller

import com.hyun.demo.dto.WordDTO
import com.hyun.demo.service.ChatService
import com.hyun.demo.service.LearningHistoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
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
        val userId = 1L
        return ResponseEntity.ok(service.getTodaysWord(userId, subject, difficulty))
    }

    @GetMapping("/word/new")
    fun getNewWord(
        @RequestParam("subject") subject: String,
        @RequestParam("difficulty") difficulty: String
    ): ResponseEntity<WordDTO>? {
        val userId = 1L
        return ResponseEntity.ok(service.getWord(userId, subject, difficulty))
    }

    @GetMapping("/sentence")
    fun getSentences(@RequestParam("word") word: String, @RequestParam("difficulty") difficulty: String): List<String> {
        return service.getSentences(word, difficulty)
    }
}