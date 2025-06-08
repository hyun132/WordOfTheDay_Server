package com.hyun.demo.word.service

import com.hyun.demo.constant.Difficulty
import com.hyun.demo.constant.toDifficulty
import com.hyun.demo.word.dto.ProfileDTO
import com.hyun.demo.word.dto.request.ProfileUpdateRequest
import com.hyun.demo.word.entity.Profile
import com.hyun.demo.word.repository.ProfileRepository
import com.hyun.demo.word.toDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class ProfileService(private val profileRepository: ProfileRepository) {
    fun createProfile(id: Long, request: ProfileUpdateRequest): ProfileDTO {
        val profile = Profile(
            id = id,
            username = request.username.orRandom(),
            topic = request.topic.orDefaultTopic(),
            difficulty = request.difficulty.orDefaultDifficulty()
        )
        return profileRepository.save(profile).toDTO()
    }

    fun getProfile(id: Long): ProfileDTO? {
        return profileRepository.findById(id).getOrNull()?.toDTO()
    }

    @Transactional
    fun updateProfile(id: Long, request: ProfileUpdateRequest): ProfileDTO? {
        val profile = profileRepository.findById(id).getOrNull() ?: return null

        request.username?.let { profile.username = it }
        request.topic?.let { profile.topic = it }
        request.difficulty?.toDifficulty().let {
            if (it != null) {
                profile.difficulty = it
            } else return null
        }

        return profile.toDTO()
    }

    private fun String?.orRandom(): String =
        this ?: UUID.randomUUID().toString()

    private fun String?.orDefaultTopic(): String =
        this ?: "travel"

    private fun String?.orDefaultDifficulty(): Difficulty =
        this?.toDifficulty() ?: Difficulty.BEGINNER
}