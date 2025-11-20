package com.kio.qit.exception

enum class ErrorCode(val code: String, val message: String) {

    // ===== LLM 관련 에러 코드 =====
    LLM_API_CALL_FAILED("500", "LLM API 호출에 실패했습니다"),
    LLM_INVALID_REQUEST("400", "LLM 요청이 유효하지 않습니다"),
    LLM_EMPTY_PROMPT("400", "LLM 프롬프트가 비어있습니다"),
    LLM_RESPONSE_PARSING_ERROR("500", "LLM 응답을 파싱하는 중 오류가 발생했습니다"),
    LLM_NULL_RESPONSE("500", "LLM 응답이 null입니다"),
    LLM_UNSUPPORTED_MODEL("501", "지원하지 않는 LLM 모델입니다"),
    LLM_MODEL_NOT_FOUND("404", "LLM 모델을 찾을 수 없습니다")

}