package com.kio.qit.enum

enum class QuestionGenerationType(val type: String) {
    CUSTOM("custom question"),
    AI("ai generated question"),
    EXISTING("existing question")
}