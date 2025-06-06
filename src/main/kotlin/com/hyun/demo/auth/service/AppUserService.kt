package com.hyun.demo.auth.service

import com.hyun.demo.auth.dto.AppUserDTO
import com.hyun.demo.auth.dto.request.PasswordUpdateRequest
import com.hyun.demo.auth.dto.request.RegisterAppUserRequest
import com.hyun.demo.auth.entity.AppUser
import com.hyun.demo.auth.repository.AppUserRepository
import com.hyun.demo.auth.toDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class AppUserService(private val appUserRepository: AppUserRepository) {

    fun registerUser(request: RegisterAppUserRequest): AppUserDTO? {
        if (appUserRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("이미 사용 중인 이메일 입니다.")
        }

        val newAppUser = AppUser(
            email = request.email,
            password = request.password
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
    fun updateUser(request:PasswordUpdateRequest):String?{
        val user = appUserRepository.findByEmail(email = request.eamil)
        user?.password = request.password
        return user?.email
    }

}