package com.kio.qit.exception

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 애플리케이션 전역에서 발생하는 예외를 처리하는 역할을 합니다.
 * 라이브러리를 사용하는 서비스에서 별도의 ExceptionHandler가 정의되어 있지 않은 경우에만 이 핸들러가 적용됩니다.
 */
@ConditionalOnMissingBean
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException::class)
    fun handleBusinessLogicException(ex: BusinessLogicException): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.valueOf(ex.errorCode.code))
            .body(mapOf("message" to (ex.message ?: "오류가 발생했습니다")))
    }
}