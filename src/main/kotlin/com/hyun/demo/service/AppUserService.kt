package com.hyun.demo.service

import com.hyun.demo.constant.Difficulty
import com.hyun.demo.dto.AppUserDTO
import com.hyun.demo.dto.request.RegisterAppUserRequest
import com.hyun.demo.dto.request.UserInfoUpdateRequest
import com.hyun.demo.entity.AppUser
import com.hyun.demo.repository.AppUserRepository
import com.hyun.demo.util.toDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrElse
import kotlin.jvm.optionals.getOrNull

@Service
class AppUserService(private val appUserRepository: AppUserRepository) {

    fun registerUser(request: RegisterAppUserRequest): AppUserDTO? {
        if (appUserRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일 입니다.")
        }
        var difficulty = Difficulty.BEGINNER
        try {
            difficulty = Difficulty.valueOf(request.difficulty.uppercase(Locale.getDefault()))
        } catch (e: Exception) {
            return null
        }

        val newAppUser = AppUser(
            email = request.email,
            username = request.username,
            password = request.password,
            difficulty = difficulty,
            topic = request.topic
        )
        return appUserRepository.save(newAppUser).toDTO()
    }

    fun getUser(email: String): AppUserDTO? {
        return appUserRepository.findByEmail(email = email)?.toDTO()
    }

    fun getUser(id: Long): AppUserDTO? {
        return appUserRepository.findById(id).getOrNull()?.toDTO()
    }

    @Transactional
    fun updateUser(id: Long, request: UserInfoUpdateRequest): AppUserDTO? {
        val user = appUserRepository.findById(id).getOrNull() ?: return null

        request.topic?.let { user.topic = it }
        request.username?.let { user.username = it }

        request.difficulty?.let { difficultyStr ->
            val difficulty = runCatching {
                Difficulty.valueOf(difficultyStr.uppercase(Locale.getDefault()))
            }.getOrElse {
                return null // 유효하지 않은 difficulty 값
            }
            user.difficulty = difficulty
        }

        return user.toDTO()
    }
}