package com.hyun.demo.word.controller

import com.hyun.demo.word.dto.ProfileDTO
import com.hyun.demo.word.dto.request.ProfileUpdateRequest
import com.hyun.demo.word.service.ProfileService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val profileService: ProfileService
) {

    @PostMapping
    fun createProfile(@RequestBody profileDTO: ProfileUpdateRequest): ResponseEntity<ProfileDTO> {
        val userId = getUserIdFromContext()

        val result = profileService.createProfile(id = userId.toLong(), request = profileDTO)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/update")
    fun updateProfile(@RequestBody request: ProfileUpdateRequest): ResponseEntity<ProfileDTO> {
        val userId = getUserIdFromContext()

        val result = profileService.updateProfile(id = userId.toLong(), request = request)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun getProfile(): ResponseEntity<ProfileDTO> {
        val userId = getUserIdFromContext()

        val count = profileService.getProfile(id = userId)
        return ResponseEntity.ok(count)
    }

    fun getUserIdFromContext(): Long {
        return (SecurityContextHolder.getContext().authentication.principal as String).toLong()
    }
}