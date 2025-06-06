package com.hyun.demo.word.controller

import com.hyun.demo.word.dto.LearningHistoryDTO
import com.hyun.demo.word.dto.WordDTO
import com.hyun.demo.word.dto.response.LearningHistoriesResponse
import com.hyun.demo.word.service.LearningHistoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/me/learning-history")
class LearningHistoryController(
    private val learningHistoryService: LearningHistoryService
) {

    @PostMapping
    fun createHistory(@RequestBody wordDTO: WordDTO): ResponseEntity<LearningHistoryDTO> {
        val userId = 1L

//        if (authentication.name.toLong() != userId) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
//        }

        val result = learningHistoryService.createWordHistory(userId = userId, word = wordDTO)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun getLearningHistory(@RequestParam("yearMonth") yearMonth: String): ResponseEntity<LearningHistoriesResponse> {
        val userId = 1L

//        if (authentication.name.toLong() != userId) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
//        }

        val list = learningHistoryService.getAllHistory(userId = userId, yearMonth)

        return ResponseEntity.ok(list)
    }

    @GetMapping("/count")
    fun getLearningHistoryCount(): ResponseEntity<Long> {
        val userId = 1L

        val count = learningHistoryService.getWordHistoryCount(userId = userId)
        return ResponseEntity.ok(count)
    }
}