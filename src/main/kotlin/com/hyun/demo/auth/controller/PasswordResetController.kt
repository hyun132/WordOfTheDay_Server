package com.hyun.demo.auth.controller

import com.hyun.demo.auth.dto.request.EmailRequest
import com.hyun.demo.auth.dto.request.EmailVerifyRequest
import com.hyun.demo.auth.dto.request.ResetPasswordRequest
import com.hyun.demo.auth.service.AppUserService
import com.hyun.demo.auth.service.EmailService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/auth/email")
class PasswordResetController(
    private val emailService: EmailService,
    private val userService: AppUserService,
    private val redisTemplate: StringRedisTemplate
) {
    @PostMapping("/verify-request")
    fun sendCode(@RequestBody request: EmailRequest): ResponseEntity<String> {
        val code = (100000..999999).random().toString()
        redisTemplate.opsForValue().set("verify:${request.email}", code, 10, TimeUnit.MINUTES)
        emailService.sendVerificationEmail(request.email, code)
        return ResponseEntity.ok("Verification code sent")
    }

    @PostMapping("/verify")
    fun verifyCode(@RequestBody request: EmailVerifyRequest): ResponseEntity<Any> {
        val saved = redisTemplate.opsForValue().get("verify:${request.email}")
        if (saved == request.code) {
            redisTemplate.delete("verify:${request.email}")
            redisTemplate.opsForValue().set("verified:${request.email}", "true", 30, TimeUnit.MINUTES)
            return ResponseEntity.ok(mapOf("verified" to true))
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("verified" to false))
    }

    @PostMapping("/password-reset")
    fun resetPassword(@RequestBody request: ResetPasswordRequest): ResponseEntity<String> {
        val verified = redisTemplate.opsForValue().get("verified:${request.email}")
        if (verified == "true") {
            userService.resetPassword(request)
            redisTemplate.delete("verified:${request.email}")
            return ResponseEntity.ok("Password updated successfully")
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Email not verified")
    }
}