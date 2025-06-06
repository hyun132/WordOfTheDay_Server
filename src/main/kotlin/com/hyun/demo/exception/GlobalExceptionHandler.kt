package com.hyun.demo.exception

import com.hyun.demo.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors
            .associate { it.field to (it.defaultMessage ?: "Invalid value") }

        val response = ErrorResponse(
            message = "유효성 검사 실패",
            status = HttpStatus.BAD_REQUEST.value(),
            errors = errors
        )

        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}