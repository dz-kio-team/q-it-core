package com.kio.qit.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class QuestionGenerationType(
    @get:JsonValue
    val type: String,
    val description: String
) {
    CUSTOM("CUSTOM", "사용자 정의 질문"),
    AI("AI", "AI 생성 질문"),
    EXISTING("EXISTING", "기존 질문")
    ;

    companion object {
        private val map: Map<String, QuestionGenerationType> = QuestionGenerationType.entries.associateBy { it.type }

        @JsonCreator
        @JvmStatic
        fun of(type: String): QuestionGenerationType {
            return map[type]
                ?: throw IllegalArgumentException("유효하지 않은 QuestionGenerationType 타입: $type")
        }
    }
}
