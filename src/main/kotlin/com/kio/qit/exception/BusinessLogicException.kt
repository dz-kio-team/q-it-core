package com.kio.qit.exception

open class BusinessLogicException(
    val errorCode: ErrorCode,
    override val message: String? = errorCode.message
) : RuntimeException(errorCode.message) {
}