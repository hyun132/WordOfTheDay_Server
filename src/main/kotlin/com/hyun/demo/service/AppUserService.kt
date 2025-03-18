package com.hyun.demo.service

import com.hyun.demo.constant.Difficulty
import com.hyun.demo.dto.AppUserDTO
import com.hyun.demo.dto.RegisterAppUserDto
import com.hyun.demo.entity.AppUser
import com.hyun.demo.repository.AppUserRepository
import com.hyun.demo.util.toDto
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppUserService(private val appUserRepository: AppUserRepository) {

    fun registerUser(registerAppUserDto: RegisterAppUserDto): AppUserDTO {
        if (appUserRepository.existsByUsername(registerAppUserDto.username)) {
            throw IllegalArgumentException("이미 사용 중인 username입니다.")
        }

        val newAppUser = AppUser(
            username = registerAppUserDto.username,
            password = registerAppUserDto.password,
            difficulty = Difficulty.valueOf(registerAppUserDto.difficulty.uppercase(Locale.getDefault()))
        )
        return appUserRepository.save(newAppUser).toDto()
    }

    fun getUser(username: String): AppUserDTO {
        return appUserRepository.findByUsername(username = username).toDto()
    }
}