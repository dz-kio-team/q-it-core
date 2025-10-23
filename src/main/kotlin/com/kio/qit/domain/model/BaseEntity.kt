package com.kio.qit.domain.model

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null
        set(value) {
            if (field != null) {
                throw IllegalAccessException("createdAt은 최초 생성 시에만 설정할 수 있습니다.")
            }
            field = value
        }

    @LastModifiedDate
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null
}