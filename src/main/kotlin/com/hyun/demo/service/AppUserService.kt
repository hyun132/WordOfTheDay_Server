package com.hyun.demo.service

import com.hyun.demo.constant.Difficulty
import com.hyun.demo.dto.AppUserDTO
import com.hyun.demo.dto.RegisterAppUserDto
import com.hyun.demo.entity.AppUser
import com.hyun.demo.repository.AppUserRepository
import com.hyun.demo.util.toDTO
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppUserService(private val appUserRepository: AppUserRepository) {

    fun registerUser(registerAppUserDto: RegisterAppUserDto): AppUserDTO {
        if (appUserRepository.existsByEmail(registerAppUserDto.email)) {
            throw IllegalArgumentException("이미 사용 중인 username입니다.")
        }

        val newAppUser = AppUser(
            email = registerAppUserDto.email,
            username = registerAppUserDto.username,
            password = registerAppUserDto.password,
            difficulty = Difficulty.valueOf(registerAppUserDto.difficulty.uppercase(Locale.getDefault()))
        )
        return appUserRepository.save(newAppUser).toDTO()
    }

    fun getUser(email: String): AppUserDTO? {
        return appUserRepository.findByEmail(email = email)?.toDTO()
    }
}