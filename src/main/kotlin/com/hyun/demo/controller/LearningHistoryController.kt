package com.hyun.demo.controller

import com.hyun.demo.dto.LearningHistoryDTO
import com.hyun.demo.dto.WordDTO
import com.hyun.demo.service.LearningHistoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/{userId}/learning-history")
class LearningHistoryController(
    private val learningHistoryService: LearningHistoryService
) {

    @PostMapping("/")
    fun createHistory(@PathVariable userId: Long, @RequestBody wordDTO: WordDTO): ResponseEntity<LearningHistoryDTO> {
        val userId = 1L

//        if (authentication.name.toLong() != userId) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
//        }

        val result = learningHistoryService.createWordHistory(userId = userId, word = wordDTO)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/")
    fun getLearningHistory(@PathVariable userId: Long): ResponseEntity<List<LearningHistoryDTO>> {
        val userId = 1L

//        if (authentication.name.toLong() != userId) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
//        }

        val list = learningHistoryService.getAllHistory(userId = userId)

        return ResponseEntity.ok(list)
    }
}