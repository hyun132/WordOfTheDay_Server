package com.hyun.demo.word.service

import com.hyun.demo.constant.Progress
import com.hyun.demo.word.dto.WordDTO
import com.hyun.demo.word.entity.LearningHistory
import com.hyun.demo.word.repository.LearningHistoryRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class LearningHistoryServiceTest {

    @MockK
    private lateinit var learningHistoryRepository: LearningHistoryRepository

    private lateinit var learningHistoryService: LearningHistoryService

    val userId = 1L
    val word = WordDTO("test", "테스트")
    val now = LocalDateTime.now()
    val learningHistory = LearningHistory(
        userId = userId,
        word = word.word,
        meaning = word.meaning,
        id = 1L,
        isDone = Progress.NOT_STARTED,
    ).apply { createdDateTime = now }

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        learningHistoryService = LearningHistoryService(learningHistoryRepository)
    }

    @Test
    fun `지금 생성하려는 학습기록과 같은 단어가 학습되지 않은 상태로 있는 경우, 새로 학습 기록을 생성하지 않고 이미 있는 단어를 업데이트해줘야함`() {
        //given
        every {
            learningHistoryRepository.findByUserIdAndWordAndMeaningOrderByCreatedDateTimeDesc(
                userId, word.word, word.meaning
            )
        } returns listOf(learningHistory)

        val updatedHistory = learningHistory.copy(isDone = Progress.COMPLETED).apply {
            createdDateTime = now
        }
        every {
            learningHistoryRepository.save(any())
        } returns updatedHistory

        //when
        val result = learningHistoryService.createWordHistory(userId, word)

        //then
        assertEquals(result.word, word.word)
        verify(exactly = 1) { learningHistoryRepository.save(any()) }
    }
}