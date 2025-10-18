package com.kio.qit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class QItApplication

fun main(args: Array<String>) {
    runApplication<QItApplication>(*args)
}
