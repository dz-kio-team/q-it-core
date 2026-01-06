package com.kio.qit.enums

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class InterviewType(
    @get:JsonValue
    val type: String,
    val description: String
) {
    HARD_SKILL("Hard Skill", "기술 면접"),
    SOFT_SKILL("Soft Skill", "인성 면접"),
    ALL("ALL", "기술 및 인성 면접")
    ;

    companion object {
        private val map: Map<String, InterviewType> = InterviewType.entries.associateBy { it.name }

        @JsonCreator
        @JvmStatic
        fun of(type: String): InterviewType {
            return map[type]
                ?: throw IllegalArgumentException("유효하지 않은 InterviewType 타입: $type")
        }
    }
}