package com.kio.qit.exception

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * 라이브러리를 사용하는 서비스에서 처리되지 않은 예외는 이 핸들러로 전달됩니다
 * 서비스에서는 `@Order(Ordered.HIGHEST_PRECEDENCE)`를 지정하여
 * 핸들러를 구현하면 GlobalExceptionHandler보다 우선적으로 예외를 처리할 수 있습니다
 */
@Order(Ordered.LOWEST_PRECEDENCE)
@ConditionalOnWebApplication    // 웹 애플리케이션에서만 활성화 (Batch 서비스 등에서는 비활성화)
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException::class)
    fun handleBusinessLogicException(ex: BusinessLogicException): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.valueOf(ex.errorCode.code))
            .body(mapOf("message" to (ex.message ?: "오류가 발생했습니다")))
    }
}