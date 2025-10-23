package com.kio.qit.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

/**
 * WebAdapter 어노테이션은 애플리케이션의 웹 계층(컨트롤러 계층)을 나타내는 클래스에 붙이는 어노테이션입니다.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class WebAdapter(

    @get:AliasFor(annotation = Component::class)
    val value: String = ""
)