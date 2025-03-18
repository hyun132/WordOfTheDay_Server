package com.hyun.demo.service

import com.hyun.demo.constant.Difficulty
import com.hyun.demo.entity.AppUser
import com.hyun.demo.entity.LearningHistory
import com.hyun.demo.repository.AppUserRepository
import com.hyun.demo.repository.LearningHistoryRepository
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LearningHistoryServiceTest(
    @Autowired val learningHistoryRepository: LearningHistoryRepository,
    @Autowired val appUserRepository: AppUserRepository
) {
    val user1 = AppUser(id = 1, username = "tester1", password = "pw1", difficulty = Difficulty.ADVANCED)

    @Test
    fun createHistory() {
        val newHistory = LearningHistory(userId = user1.id ?: 1, word = "test")
        learningHistoryRepository.save(newHistory)
    }

    @Test
    fun getAllHistory() {
        val newHistory = LearningHistory(userId = 1, word = "test")
        learningHistoryRepository.save(newHistory)

        val histories = learningHistoryRepository.findAllByUserIdOrderByCreatedDateTimeDesc(user1.id ?: 1)
        assert(histories.isNotEmpty())
        assert(histories.size == 1)
    }

}