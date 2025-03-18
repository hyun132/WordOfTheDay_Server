package com.hyun.demo.controller

import com.hyun.demo.service.ChatService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(private val service: ChatService) {

    @GetMapping("/chat")
    fun chat(@RequestParam("message") message: String): String? {
        return service.plainTextChat(message = message)
    }

    @GetMapping("/word")
    fun getWord(@RequestParam("subject") subject: String, @RequestParam("target") target: String): String? {
        return service.getWord(subject, target)
    }

    @GetMapping("/sentence")
    fun getSentences(@RequestParam("word") word: String, @RequestParam("target") target: String): List<String> {
        return service.getSentences(word, target)
    }
}