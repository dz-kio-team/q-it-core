package com.kio.qit.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.MDC
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskDecorator
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

/**
 * 비동기 작업을 처리하기 위한 스레드 풀 설정을 구성합니다.
 * 환경 설정에서 `qit.async.enabled`가 `true`로 설정된 경우에만 이 구성이 적용됩니다.
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(
    prefix = "qit.async",
    name = ["enabled"],
    havingValue = "true",
)
@EnableAsync
class AsyncConfig : AsyncConfigurer {

    private val log = KotlinLogging.logger {}

    @Bean
    override fun getAsyncExecutor(): Executor? {
        return ThreadPoolTaskExecutor().apply {
            corePoolSize = 32    // 기본 스레드 수
            maxPoolSize = 32    // 최대 스레드 수
            queueCapacity = 1000000  // 큐 용량
            setThreadNamePrefix("async-")   // 스레드 이름 접두사
            setAwaitTerminationSeconds(5) // 종료 대기 시간
            setWaitForTasksToCompleteOnShutdown(true) // 종료 시 작업 완료 대기
            setTaskDecorator(MdcTaskDecorator()) // MDC 데코레이터 설정
            initialize()
            threadPoolExecutor.prestartAllCoreThreads() // 모든 코어 스레드 미리 시작
        }
    }

    /**
     * 비동기 메서드 실행 중 발생하는 예외를 처리하는 핸들러를 정의합니다.
     */
    override fun getAsyncUncaughtExceptionHandler() =
        AsyncUncaughtExceptionHandler { throwable, method, params ->
            log.error { "Exception Caught in Thread - ${Thread.currentThread().name}" }
            log.error { "Exception message - ${throwable.message}" }
            log.error { "Method name - ${method.name}" }
            params.forEach { param -> log.error { "Parameter value - $param" } }
        }

    /**
     * MDC 컨텍스트를 Async Task에 전달하는 역할을 합니다.
     */
    class MdcTaskDecorator : TaskDecorator {
        override fun decorate(runnable: Runnable): Runnable {
            val contextMap = MDC.getCopyOfContextMap()
            return Runnable {
                try {
                    if (contextMap != null) {
                        MDC.setContextMap(contextMap)
                    }
                    runnable.run()
                } finally {
                    MDC.clear()
                }
            }
        }
    }
}