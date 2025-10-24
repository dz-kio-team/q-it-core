package com.kio.qit.enum

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class QuestionGenerationType(
    @get:JsonValue
    val type: String,
    val description: String
) {
    CUSTOM("custom question", "사용자 정의 질문"),
    AI("AI generated question", "AI 생성 질문"),
    EXISTING("existing question", "기존 질문")
    ;

    companion object {
        @JsonCreator
        @JvmStatic
        fun of(type: String): QuestionGenerationType {
            return entries.firstOrNull { it.type == type }
                ?: throw IllegalArgumentException("유효하지 않은 QuestionGenerationType 타입: $type")
        }
    }
}
