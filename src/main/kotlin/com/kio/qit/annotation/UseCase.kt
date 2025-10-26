package com.kio.qit.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

/**
 * UseCase 어노테이션은 애플리케이션의 유스케이스 계층(비즈니스 로직 계층)을 나타내는 클래스에 붙이는 어노테이션입니다.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class UseCase(

    @get:AliasFor(annotation = Component::class)
    val value: String = ""
)
