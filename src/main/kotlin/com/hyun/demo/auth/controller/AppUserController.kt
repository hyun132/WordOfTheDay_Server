package com.hyun.demo.auth.controller

import com.hyun.demo.auth.dto.AppUserDTO
import com.hyun.demo.auth.dto.request.PasswordUpdateRequest
import com.hyun.demo.auth.dto.request.RegisterAppUserRequest
import com.hyun.demo.auth.dto.response.RegisterAppUserResponse
import com.hyun.demo.auth.service.AppUserService
import com.hyun.demo.word.dto.response.UserInfoUpdateResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("api/auth")
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
    fun update(@RequestBody updateRequest: PasswordUpdateRequest): ResponseEntity<UserInfoUpdateResponse> {
        val id = 1
        val email = appUserService.updatePassword(1,updateRequest)
        if (email == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
        } else
            return ResponseEntity.ok(
                UserInfoUpdateResponse(
                    email = email,
                )
            )
    }
}