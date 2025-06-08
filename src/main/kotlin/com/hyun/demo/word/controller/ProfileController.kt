package com.hyun.demo.word.controller

import com.hyun.demo.word.dto.ProfileDTO
import com.hyun.demo.word.dto.request.ProfileUpdateRequest
import com.hyun.demo.word.service.ProfileService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val profileService: ProfileService
) {

    @PostMapping
    fun createProfile(@RequestBody profileDTO: ProfileUpdateRequest): ResponseEntity<ProfileDTO> {
        val userId = 1L

//        if (authentication.name.toLong() != userId) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
//        }

        val result = profileService.createProfile(id = userId, request = profileDTO)
        return ResponseEntity.ok(result)
    }

    @PostMapping("update")
    fun updateProfile(@RequestBody request: ProfileUpdateRequest): ResponseEntity<ProfileDTO> {
        val userId = 1L

//        if (authentication.name.toLong() != userId) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
//        }

        val result = profileService.updateProfile(id = userId, request = request)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun getProfile(): ResponseEntity<ProfileDTO> {
        val userId = 1L

        val count = profileService.getProfile(id = userId)
        return ResponseEntity.ok(count)
    }
}