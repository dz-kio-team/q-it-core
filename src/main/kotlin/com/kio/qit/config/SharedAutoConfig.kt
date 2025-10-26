package com.kio.qit.config

import com.kio.qit.exception.GlobalExceptionHandler
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Import

/**
 * 라이브러리의 스프링 빈은 다른 서비스에서 Component Scan 대상이 아니기 때문에,
 * 라이브러리를 사용하는 서비스가 라이브러리에서 어떤 구성 요소를 스캔할지 결정할 수 있도록 별도의 설정 클래스를 제공합니다.
 */
@AutoConfiguration
@Import(
    value = [
        // 여기에 라이브러리의 스프링 빈 구성 요소들을 추가합니다.
        JacksonConfig::class,
        AsyncConfig::class,
        GlobalExceptionHandler::class
    ]
)
class SharedAutoConfig {
}