package com.hyun.demo.service

import com.hyun.demo.service.word.ChatService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.ai.chat.client.ChatClient

@ExtendWith(MockitoExtension::class)
class ChatServiceTest {

    @InjectMocks
    lateinit var chatService: ChatService

    @Mock
    lateinit var chatClient: ChatClient

    @Test
    fun filterResponseData() {
        val result = listOf(
            "1. I'm willing to compromise on the price, but I expect a discount of at least 10%.",
            "2. Can you tell me more about what's included in this offer?",
            "3. I can give you an extra week to pay if you agree to the current price.",
            "4. Would you be willing to meet me halfway on the price of this product?",
            "5. Let me see if I can get a better deal elsewhere, and we can revisit the negotiation after that."
        )
        val strings = """Here are five sample sentences on negotiation for intermediate learners:
        
        1. I'm willing to compromise on the price, but I expect a discount of at least 10%.
        2. Can you tell me more about what's included in this offer?
        3. I can give you an extra week to pay if you agree to the current price.
        4. Would you be willing to meet me halfway on the price of this product?
        5. Let me see if I can get a better deal elsewhere, and we can revisit the negotiation after that."""

        val list = strings.split("\n").map { it.trim() }.filter { it.matches(Regex("^\\d+\\..*")) }.toList()
        println(list)
        assert(list == result)
    }
}