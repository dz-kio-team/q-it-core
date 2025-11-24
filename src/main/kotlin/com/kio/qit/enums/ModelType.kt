package com.kio.qit.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class ModelType(
    @get:JsonValue
    val type: String,
    val description: String
) {
    GPT_4("gpt-4", "질문 생성에 사용되는 모델"),
    OLLAMA("ollama", "질문 정제에 사용되는 모델"),
    ;

    companion object {
        private val map: Map<String, ModelType> = entries.associateBy { it.type }

        @JsonCreator
        @JvmStatic
        fun of(type: String): ModelType {
            return map[type]
                ?: throw IllegalArgumentException("유효하지 않은 ModelType 타입: $type. 지원하는 타입: ${map.keys.joinToString(", ")}")
        }
    }
}