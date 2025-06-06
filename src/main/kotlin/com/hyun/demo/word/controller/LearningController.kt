package com.hyun.demo.word.controller

import com.hyun.demo.word.dto.request.AnswerSubmitRequest
import com.hyun.demo.word.dto.response.AnswerCheckResponse
import com.hyun.demo.word.service.LearningService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class LearningController(
    val service: LearningService
) {
    @PostMapping("/learning/answer")
    fun checkAnswer(@RequestBody @Valid request: AnswerSubmitRequest): ResponseEntity<AnswerCheckResponse> {
        val result = service.checkAnswer(request)
        return ResponseEntity.ok(result)
    }
}