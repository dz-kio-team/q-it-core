package com.kio.qit.enum

enum class InterviewType(val type: String) {
    HARD_SKILL("hard skill"),
    SOFT_SKILL("soft skill"),
    ALL("all")
    ;

    companion object {
        fun of(type: String): InterviewType {
            return entries.first { it.type == type }
        }
    }
}