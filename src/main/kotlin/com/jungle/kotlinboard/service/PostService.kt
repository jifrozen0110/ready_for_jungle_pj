package com.jungle.kotlinboard.service

import com.jungle.kotlinboard.common.auth.Role
import com.jungle.kotlinboard.common.exception.NotAuthorizedException
import com.jungle.kotlinboard.common.exception.PostNotFoundException
import com.jungle.kotlinboard.domain.MemberRoleRepository
import com.jungle.kotlinboard.domain.Post
import com.jungle.kotlinboard.domain.PostRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PostService(
    private val postRepository: PostRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val memberService: MemberService,
) {
    @Transactional(readOnly = true)
    fun getPost(postId: Long): PostResponse {
        val post = postRepository.findById(postId).orElseThrow()
        return PostResponse(
            id = post.id,
            title = post.title,
            content = post.content,
            createdDate = post.createdDate,
            createdBy = post.createdBy,
        )
    }

    @Transactional(readOnly = true)
    fun getPosts(): List<Post> {
        val sort = Sort.by(Sort.Direction.DESC, "createdDate")
        return postRepository.findAll(sort)
    }

    fun insertPost(
        userId: Long,
        postCreateRequest: PostCreateRequest,
    ): PostResponse {
        val post: Post =
            postRepository.save(
                Post.of(
                    title = postCreateRequest.title,
                    content = postCreateRequest.content,
                    userId = userId,
                ),
            )

        return PostResponse(
            id = post.id,
            title = post.title,
            content = post.content,
            createdDate = post.createdDate,
            createdBy = post.createdBy,
        )
    }

    fun updatePost(
        postId: Long,
        userId: Long,
        postUpdateRequest: PostUpdateRequest,
    ) {
        val post =
            postRepository.findById(postId).orElseThrow {
                PostNotFoundException(postId)
            }
        val member =
            memberService.findMember(userId)
        val isAdmin = memberRoleRepository.existsByMemberAndRole(member, Role.ADMIN)

        if (member.id != post.userId && !isAdmin) {
            throw NotAuthorizedException("작성자만 수정할 수 있습니다.")
        }

        if (!postUpdateRequest.title.isNullOrBlank()) {
            post.updateTitle(postUpdateRequest.title)
        }

        if (!postUpdateRequest.content.isNullOrBlank()) {
            post.updateContent(postUpdateRequest.content)
        }
    }

    fun deletePost(
        id: Long,
        userId: Long,
    ) {
        val post = postRepository.findById(id).orElseThrow()
        if (userId != post.userId) {
            NotAuthorizedException("작성자만 삭제할 수 있습니다.")
        }
        postRepository.delete(post)
    }
}
