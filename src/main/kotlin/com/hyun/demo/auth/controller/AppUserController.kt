package com.hyun.demo.auth.controller

import com.hyun.demo.auth.dto.AppUserDTO
import com.hyun.demo.auth.dto.request.LoginRequest
import com.hyun.demo.auth.dto.request.PasswordUpdateRequest
import com.hyun.demo.auth.dto.request.RefreshRequest
import com.hyun.demo.auth.dto.request.RegisterAppUserRequest
import com.hyun.demo.auth.dto.response.CheckEmailResponse
import com.hyun.demo.auth.dto.response.TokenPair
import com.hyun.demo.auth.dto.response.RegisterAppUserResponse
import com.hyun.demo.auth.service.AppUserService
import com.hyun.demo.word.dto.response.UserInfoUpdateResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AppUserController(
    private val appUserService: AppUserService
) {

    @PostMapping("/signup")
    fun register(
        @RequestBody @Valid request: RegisterAppUserRequest
    ): ResponseEntity<RegisterAppUserResponse> {
        val user = appUserService.registerUser(request)

        return ResponseEntity.ok(
            RegisterAppUserResponse(
                email = user.email,
                createdAt = user.createdAt
            )
        )
    }

    @GetMapping("/check-email")
    fun checkValidEmail(@RequestParam email: String): ResponseEntity<CheckEmailResponse> {
        val available = !appUserService.checkEmailDuplication(email = email)
        return ResponseEntity.ok(CheckEmailResponse(available))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<TokenPair> {
        val token = appUserService.login(loginRequest) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "사용자를 찾을 수 없습니다."
        )
        return ResponseEntity.ok(token)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody refreshRequest: RefreshRequest): ResponseEntity<TokenPair> {
        val token = appUserService.refresh(refreshRequest.refreshToken)
        return ResponseEntity.ok(token)
    }


    @PostMapping("/update")
    fun updatePassword(@Valid @RequestBody updateRequest: PasswordUpdateRequest): ResponseEntity<UserInfoUpdateResponse> {
        val userId = SecurityContextHolder.getContext().authentication.principal as String
        val email = appUserService.updatePassword(userId.toLong(), updateRequest)
        return ResponseEntity.ok(
            UserInfoUpdateResponse(
                email = email,
            )
        )
    }
}