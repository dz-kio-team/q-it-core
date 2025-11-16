package com.kio.qit.enums

import com.fasterxml.jackson.annotation.JsonCreator

enum class LlmMessageType(
    val type: String,
    val description: String
) {
    SYSTEM("system", "모델의 전체 행동 규칙을 정의하는 메시지 (예: 톤, 스타일, 역할, 제약 등)"),
    USER("user", "사용자가 모델에게 요청하는 실제 메시지"),
    ASSISTANT("assistant", "모델이 이전에 응답한 메시지 (대화 맥락 유지에 사용)"),
    ;

    companion object {
        @JsonCreator
        @JvmStatic
        fun of(type: String): LlmMessageType {
            return LlmMessageType.entries.firstOrNull { it.type == type }
                ?: throw IllegalArgumentException("유효하지 않은 LlmMessageType 타입: $type")
        }
    }
}