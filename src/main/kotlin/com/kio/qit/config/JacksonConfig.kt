package com.kio.qit.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.kio.qit.constant.Constant.DATETIME_FORMAT
import com.kio.qit.utils.DateUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Jackson의 ObjectMapper 설정을 구성합니다.
 * Kotlin 모듈과 Java Time 모듈을 등록하고, LocalDateTime에 대한 커스텀 직렬화/역직렬화를 설정합니다.
 * 라이브러리를 사용하는 서비스에 ObjectMapper 빈이 없는 경우에만 이 구성이 적용됩니다.
 */
@Configuration(proxyBeanMethods = false)
class JacksonConfig {

    @Bean
    @ConditionalOnMissingBean(ObjectMapper::class)
    fun objectMapper(): ObjectMapper {
        val javaTimeModule = JavaTimeModule()
            .addSerializer(
                LocalDateTime::class.java,
                CustomLocalDateTimeSerializer()
            )
            .addDeserializer(
                LocalDateTime::class.java,
                CustomLocalDateTimeDeserializer()
            )

        val kotlinModule = KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)  // 컬렉션 타입에 null 대신 빈 컬렉션 사용 비활성화
            .configure(KotlinFeature.NullToEmptyMap, false)        // 맵 타입에 null 대신 빈 맵 사용 비활성화
            .configure(KotlinFeature.NullIsSameAsDefault, false)    // null을 기본값으로 간주 비활성화
            .configure(KotlinFeature.SingletonSupport, false)     // 싱글톤 객체 지원 비활성화
            .configure(KotlinFeature.NewStrictNullChecks, false)    // 새로운 엄격한 null 검사 비활성화
            .build()

        return ObjectMapper()
            .registerModules(kotlinModule, javaTimeModule)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)    // 날짜를 타임스탬프 숫자 형식으로 쓰지 않고 문자열 형식으로 쓰도록 설정
    }

    class CustomLocalDateTimeSerializer() : JsonSerializer<LocalDateTime>() {
        override fun serialize(
            value: LocalDateTime?,
            gen: JsonGenerator?,
            serializers: SerializerProvider?
        ) {
            gen?.writeString(DateTimeFormatter.ofPattern(DATETIME_FORMAT, Locale.KOREA).format(value))
        }
    }

    class CustomLocalDateTimeDeserializer() : JsonDeserializer<LocalDateTime>() {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDateTime {
            return DateUtils.parse(p!!.text, DATETIME_FORMAT)
        }
    }
}