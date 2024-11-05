package com.jungle.kotlinboard.domain

import com.jungle.kotlinboard.common.auth.Role
import com.jungle.kotlinboard.service.MemberResponse
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(name = "uk_member_user_id", columnNames = ["user_id"])],
    name = "members",
)
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(nullable = false, unique = true, length = 100)
    val userId: String,
    @Column(nullable = false, length = 100)
    val password: String,
    @Column(nullable = false, length = 10)
    var nickName: String,
) {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    val memberRole: List<MemberRole>? = null

    companion object {
        fun of(
            userId: String,
            password: String,
            nickName: String,
        ): Member =
            Member(
                userId = userId,
                password = password,
                nickName = nickName,
            )
    }

    fun toDto(): MemberResponse = MemberResponse(id, userId, nickName)

    fun updateNickname(nickName: String) {
        this.nickName = nickName
    }

    fun checkPassword(password: String): Boolean = this.password == password
}

@Entity
class MemberRole(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    val role: Role,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = ForeignKey(name = "fk_member_role_member_id"))
    val member: Member,
    // val memberId:Long
)
