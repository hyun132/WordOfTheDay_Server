package com.hyun.demo.auth.dto.request

import com.hyun.demo.constant.Difficulty
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank


data class RegisterAppUserRequest(
    @field:Email(message = "올바른 이메일 주소를 입력해주세요.")
    @field:NotBlank(message = "이메일은 비워둘 수 없습니다.")
    var email: String,

    var username: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    var password: String,

    var topic: String = "school",

    var difficulty: String = Difficulty.BEGINNER.name
)