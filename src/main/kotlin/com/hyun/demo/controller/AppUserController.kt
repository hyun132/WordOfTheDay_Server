package com.hyun.demo.controller

import com.hyun.demo.constant.Difficulty
import com.hyun.demo.dto.AppUserDTO
import com.hyun.demo.dto.request.RegisterAppUserRequest
import com.hyun.demo.dto.request.UserInfoUpdateRequest
import com.hyun.demo.dto.response.RegisterAppUserResponse
import com.hyun.demo.dto.response.UserInfoUpdateResponse
import com.hyun.demo.service.AppUserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class AppUserController(private val appUserService: AppUserService) {

    @PostMapping("/signup")
    fun register(
        @RequestBody @Valid request: RegisterAppUserRequest
    ): ResponseEntity<RegisterAppUserResponse> {
        val user = appUserService.registerUser(request)
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "회원가입에 실패하였습니다."
            )

        return ResponseEntity.ok(
            RegisterAppUserResponse(
                email = user.email,
                username = user.username,
                topic = user.topic,
                difficulty = Difficulty.valueOf(user.difficulty.uppercase()),
                createdAt = user.createdAt
            )
        )
    }

    @GetMapping("/{email}")
    fun getUser(@PathVariable email: String): ResponseEntity<AppUserDTO> {
        var userId = 1
        val user = appUserService.getUser(email = email) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "사용자를 찾을 수 없습니다."
        )
        return ResponseEntity.ok(user)
    }

    @PostMapping("/update")
    fun update(@RequestBody updateRequest: UserInfoUpdateRequest): ResponseEntity<UserInfoUpdateResponse> {
        val user = appUserService.updateUser(1, updateRequest)
        if (user == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
        } else
            return ResponseEntity.ok(
                UserInfoUpdateResponse(
                    username = user.username,
                    difficulty = user.difficulty,
                    topic = user.topic
                )
            )
    }
}