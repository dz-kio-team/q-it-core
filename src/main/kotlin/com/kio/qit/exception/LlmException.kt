package com.kio.qit.exception

/**
 * LLM Client에서 발생하는 비즈니스 로직 예외
 *
 * @param errorCode LLM 관련 에러 코드
 * @param message 커스텀 에러 메시지
 * @param cause 원인이 된 예외
 * @param additionalInfo LLM 관련 추가 정보 (모델명 등)
 *
 */
open class LlmException(
    errorCode: ErrorCode,
    message: String? = null,
    cause: Throwable? = null,
    val additionalInfo: Map<String, Any?>? = null
) : BusinessLogicException(
    errorCode = errorCode,
    message = message ?: errorCode.message
) {
    init {
        cause?.let { initCause(it) }
    }

    constructor(
        errorCode: ErrorCode,
        modelType: String,
        message: String? = null,
        cause: Throwable? = null
    ) : this(
        errorCode = errorCode,
        message = message,
        cause = cause,
        additionalInfo = mapOf("model" to modelType)
    )
}