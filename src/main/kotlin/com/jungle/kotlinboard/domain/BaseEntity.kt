package com.jungle.kotlinboard.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_datetime", nullable = false, updatable = false)
    var createdDate: LocalDateTime = LocalDateTime.MIN
        protected set

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    var createdBy: String = "test"
        protected set
}