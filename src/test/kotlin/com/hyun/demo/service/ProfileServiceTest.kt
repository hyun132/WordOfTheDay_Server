package com.hyun.demo.service

import com.hyun.demo.constant.Difficulty
import com.hyun.demo.word.entity.Profile
import com.hyun.demo.word.repository.ProfileRepository
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.jvm.optionals.getOrNull
import kotlin.test.Test

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileServiceTest(
    @Autowired val appUserRepository: ProfileRepository
) {
    val user1 = Profile(id = 1, username = "tester1", difficulty = Difficulty.ADVANCED, topic = "")

    @Test
    fun createUser() {
        val createdUser = appUserRepository.save(user1)
        assert(user1 == createdUser)
    }

    @Test
    fun findUser() {
        val createdUser = appUserRepository.save(user1)
        val resultUser = appUserRepository.findById(user1.id).getOrNull()
        assert(createdUser == resultUser)
    }

}