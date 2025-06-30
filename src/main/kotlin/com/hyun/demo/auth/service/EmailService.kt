package com.hyun.demo.auth.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    fun sendVerificationEmail(to: String, code: String) {
        val message = SimpleMailMessage()
        message.setTo(to)
        message.setSubject("[WordOfTheDay] 비밀번호 변경 인증 코드입니다.")
        message.setText("인증 코드: $code\n\n10분 이내에 입력해주세요.")
        mailSender.send(message)
    }
}