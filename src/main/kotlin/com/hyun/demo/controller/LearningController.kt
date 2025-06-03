package com.hyun.demo.controller

import com.hyun.demo.dto.request.AnswerSubmitRequest
import com.hyun.demo.dto.response.AnswerCheckResponse
import com.hyun.demo.service.LearningService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LearningController(
    val service: LearningService
) {
    @PostMapping("/learning/answer")
    fun checkAnswer(@RequestBody @Valid request: AnswerSubmitRequest): ResponseEntity<AnswerCheckResponse> {
        val result = service.checkAnswer(request)
        return ResponseEntity.ok(result)
    }
}