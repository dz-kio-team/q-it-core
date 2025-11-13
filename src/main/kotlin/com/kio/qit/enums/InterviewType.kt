package com.kio.qit.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class InterviewType(
    @get:JsonValue
    val type: String,
    val description: String
) {
    HARD_SKILL("hard skill", "기술 면접"),
    SOFT_SKILL("soft skill", "인성 면접"),
    ALL("all", "기술 및 인성 면접")
    ;

    companion object {
        @JsonCreator
        @JvmStatic
        fun of(type: String): InterviewType {
            return entries.firstOrNull { it.type == type }
                ?: throw IllegalArgumentException("유효하지 않은 InterviewType 타입: $type")
        }
    }
}