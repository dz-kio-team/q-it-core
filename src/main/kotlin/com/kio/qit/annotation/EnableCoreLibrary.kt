package com.kio.qit.annotation

import com.kio.qit.config.SharedAutoConfig
import org.springframework.context.annotation.Import

/**
 * EnableCoreService 어노테이션은 라이브러리의 핵심 서비스 구성을 활성화하는 데 사용됩니다.
 * 이 어노테이션을 사용하면 SharedConfig에 정의된 스프링 빈들이 애플리케이션 컨텍스트에 등록됩니다.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(SharedAutoConfig::class)
annotation class EnableCoreLibrary()
