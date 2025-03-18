package com.hyun.demo.service

import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Service

@Service
class ChatService(private val chatClient: ChatClient) {

    fun plainTextChat(message: String): String {
        return chatClient
            .prompt()
            .user(message)
            .call()
            .content() ?: ""
    }

    fun getWord(subject: String, target: String): String {
        return chatClient
            .prompt()
            .user("Show me a word about $subject for $target. Just one word.")
            .call()
            .content() ?: ""
    }

    fun getSentences(word: String, target: String): List<String> {
        val answer = chatClient
            .prompt()
            .user("Show me 5 useful sample sentences using $word for $target. Just Numbered sentences only.")
            .call()
            .content()

        val list = answer?.split(",")?.filter { it.matches(Regex("^\\d+\\..*")) }?.toList() ?: emptyList()

        return list
    }
}