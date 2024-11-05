package com.jungle.kotlinboard.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(
    name = "posts",
)
class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(nullable = false, length = 255)
    var title: String,
    @Column(nullable = false, length = 6553)
    var content: String,
    @Column(nullable = false, updatable = false)
    val userId: Long,
) : BaseEntity() {
    companion object {
        fun of(
            title: String,
            content: String,
            userId: Long,
        ): Post =
            Post(
                title = title,
                content = content,
                userId = userId,
            )
    }

    fun updateTitle(title: String) {
        this.title = title
    }

    fun updateContent(content: String) {
        this.content = content
    }
}
