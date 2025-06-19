package com.hyun.demo.auth.service

import com.hyun.demo.auth.dto.AppUserDTO
import com.hyun.demo.auth.dto.request.LoginRequest
import com.hyun.demo.auth.dto.request.PasswordUpdateRequest
import com.hyun.demo.auth.dto.request.RegisterAppUserRequest
import com.hyun.demo.auth.dto.response.TokenPair
import com.hyun.demo.auth.entity.AppUser
import com.hyun.demo.auth.entity.RefreshToken
import com.hyun.demo.auth.repository.AppUserRepository
import com.hyun.demo.auth.repository.RefreshTokenRepository
import com.hyun.demo.auth.toDTO
import com.hyun.demo.security.HashEncoder
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*

@Service
class AppUserService(
    private val appUserRepository: AppUserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtService: JwtService,
    private val hashEncoder: HashEncoder
) {
    fun registerUser(request: RegisterAppUserRequest): AppUserDTO {
        require(!appUserRepository.existsByEmail(request.email)) {
            "이미 사용 중인 이메일입니다."
        }

        val user = AppUser(
            email = request.email,
            password = hashEncoder.encode(request.password)
        )
        return appUserRepository.save(user).toDTO()
    }


    fun getUser(email: String): AppUserDTO? {
        return appUserRepository.findByEmail(email)?.toDTO()
    }

    fun login(loginRequest: LoginRequest): TokenPair? {
        val user = appUserRepository.findByEmail(loginRequest.email)
            ?: throw BadCredentialsException("사용자 인증에 실패했습니다.")

        check(hashEncoder.matches(loginRequest.password, user.password)) {
            "사용자 인증에 실패했습니다."
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toString())
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toString())

        storeRefreshToken(user.id!!, newRefreshToken)

        return TokenPair(accessToken = newAccessToken, refreshToken = newRefreshToken)
    }

    @Transactional
    fun refresh(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw IllegalArgumentException("Invalid refresh token.")
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = appUserRepository.findById(userId.toLong()).orElseThrow {
            IllegalArgumentException("Invalid refresh token.")
        }

        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(userId = userId.toLong(), hashedToken = hashed)
            ?: throw IllegalArgumentException("Refresh token not recognized")

        refreshTokenRepository.deleteByUserIdAndHashedToken(userId = userId.toLong(), hashed)
        val newAccessToken = jwtService.generateAccessToken(userId = userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId = userId)

        storeRefreshToken(userId.toLong(),newRefreshToken)

        return TokenPair(
            newAccessToken,
            newRefreshToken
        )
    }

    fun checkEmailDuplication(email: String): Boolean {
        return appUserRepository.existsByEmail(email = email)
    }

    private fun storeRefreshToken(userId: Long, rawRefreshToken: String) {
        val hashed = hashToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = LocalDateTime.now().plusNanos(expiryMs)

        refreshTokenRepository.save(
            RefreshToken(
                userId,
                expiresAt,
                hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digit = MessageDigest.getInstance("SHA-256")
        val hashBytes = digit.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }

    @Transactional
    fun updatePassword(userId: Long, request: PasswordUpdateRequest): String {
        val user = appUserRepository.findById(userId)
            .orElseThrow { BadCredentialsException("사용자 인증에 실패했습니다.") }

        check(hashEncoder.matches(request.currentPassword, user.password)) {
            "현재 비밀번호가 일치하지 않습니다."
        }

        user.password = hashEncoder.encode(request.newPassword)
        return user.email
    }
}