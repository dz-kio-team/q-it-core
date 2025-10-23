package com.kio.qit.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    fun getCurrentDateTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalDateTime.now().format(formatter)
    }

    fun format(dateTime: LocalDateTime, pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(formatter)
    }

    fun parse(dateString: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return LocalDateTime.parse(dateString, formatter)
    }
}