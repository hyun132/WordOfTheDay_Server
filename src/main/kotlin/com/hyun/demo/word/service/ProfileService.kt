package com.hyun.demo.word.service

import com.hyun.demo.constant.Difficulty
import com.hyun.demo.word.dto.ProfileDTO
import com.hyun.demo.word.dto.request.ProfileUpdateRequest
import com.hyun.demo.word.repository.ProfileRepository
import com.hyun.demo.word.toDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class ProfileService(private val profileRepository: ProfileRepository) {

    fun getUser(id: Long): ProfileDTO? {
        return profileRepository.findById(id).getOrNull()?.toDTO()
    }

    @Transactional
    fun updateUser(id: Long, request: ProfileUpdateRequest): ProfileDTO? {
        val user = profileRepository.findById(id).getOrNull() ?: return null

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