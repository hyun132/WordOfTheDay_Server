package com.hyun.demo.service

import com.hyun.demo.constant.Difficulty
import com.hyun.demo.entity.AppUser
import com.hyun.demo.repository.AppUserRepository
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppUserServiceTest(
    @Autowired val appUserRepository: AppUserRepository
) {
    val user1 = AppUser(username = "tester1", password = "pw1", difficulty = Difficulty.ADVANCED, email = "test@test.com")

    @Test
    fun createUser() {
        val createdUser = appUserRepository.save(user1)
        assert(user1 == createdUser)
    }

    @Test
    fun findUser() {
        val createdUser = appUserRepository.save(user1)
        val resultUser = appUserRepository.findByEmail("test@test.com")
        assert(createdUser == resultUser)
    }

}