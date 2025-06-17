package com.hyun.demo.auth.service

import com.hyun.demo.auth.dto.AppUserDTO
import com.hyun.demo.auth.dto.request.LoginRequest
import com.hyun.demo.auth.dto.request.PasswordUpdateRequest
import com.hyun.demo.auth.dto.request.RegisterAppUserRequest
import com.hyun.demo.auth.entity.AppUser
import com.hyun.demo.auth.repository.AppUserRepository
import com.hyun.demo.auth.toDTO
import com.hyun.demo.security.HashEncoder
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AppUserService(
    private val appUserRepository: AppUserRepository,
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

    fun login(userId: Long, loginRequest: LoginRequest): AppUserDTO? {
        val user = appUserRepository.findById(userId)
            .orElseThrow { BadCredentialsException("사용자 인증에 실패했습니다.") }

        check(hashEncoder.matches(loginRequest.password, user.password)) {
            "사용자 인증에 실패했습니다."
        }

        return appUserRepository.findByEmail(user.email)?.toDTO()
    }

    fun checkEmailDuplication(email: String): Boolean {
        return appUserRepository.existsByEmail(email = email)
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