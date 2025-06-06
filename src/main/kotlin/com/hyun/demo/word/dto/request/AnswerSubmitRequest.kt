package com.hyun.demo.word.dto.request

import com.hyun.demo.constant.Mode
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class AnswerSubmitRequest (
    @field:NotBlank(message = "입력값은 비어 있을 수 없습니다.")
    val origin:String,

    val userAnswer:String,

    @field:NotNull(message = "모드는 필수입니다.")
    val mode:Mode
)