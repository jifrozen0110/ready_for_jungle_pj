package com.jungle.kotlinboard.service

import com.jungle.kotlinboard.common.auth.Role
import com.jungle.kotlinboard.common.exception.NotAuthorizedException
import com.jungle.kotlinboard.common.fixture.POST_TEST_UPDATE_TITLE
import com.jungle.kotlinboard.common.fixture.createMember
import com.jungle.kotlinboard.common.fixture.createPost
import com.jungle.kotlinboard.common.fixture.createPostTest
import com.jungle.kotlinboard.common.fixture.createPostUpdateRequest
import com.jungle.kotlinboard.domain.MemberRoleRepository
import com.jungle.kotlinboard.domain.Post
import com.jungle.kotlinboard.domain.PostRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.util.Optional

class PostServiceTest {
    private lateinit var postService: PostService
    private lateinit var postRepository: PostRepository
    private lateinit var memberService: MemberService
    private lateinit var memberRoleRepository: MemberRoleRepository

    @BeforeEach
    fun setup() {
        postRepository = mock()
        memberRoleRepository = mock()
        memberService = mock(MemberService::class.java)
        postService = PostService(postRepository, memberRoleRepository, memberService)
    }

    @Test
    fun `게시글 생성 테스트 성공`() {
        // given
        val postCreateRequest =
            PostCreateRequest(
                title = "test title",
                content = "test content",
            )

        val post =
            Post.of(
                title = "test title",
                content = "test content",
                userId = 1L,
            )

        `when`(postRepository.save(any(Post::class.java))).thenReturn(post)

        // when
        val result = postService.insertPost(1L, postCreateRequest)

        // then
        assertEquals(result.title, postCreateRequest.title)
        assertEquals(result.content, postCreateRequest.content)
    }

    @Test
    fun `게시글 수정 성공 테스트`() {
        // given
        val request = createPostUpdateRequest(POST_TEST_UPDATE_TITLE)
        val post = createPost()
        val member = createMember()
        `when`(
            postRepository.findById(1L),
        ).thenReturn(Optional.of(post))

        `when`(
            memberService.findMember(1L),
        ).thenReturn(member)

        `when`(
            memberRoleRepository.existsByMemberAndRole(member, Role.ADMIN),
        ).thenReturn(false)
        // when
        postService.updatePost(1L, 1L, request)
        // then
        assertEquals(post.title, POST_TEST_UPDATE_TITLE)
    }

    @Test
    fun `게시글 수정 실패 테스트 - NotAuthorizedException`() {
        val request = createPostUpdateRequest(POST_TEST_UPDATE_TITLE)
        val post = createPost()
        val member = createMember(id = 2L)
        `when`(
            postRepository.findById(1L),
        ).thenReturn(Optional.of(post))

        `when`(
            memberService.findMember(2L),
        ).thenReturn(member)

        `when`(
            memberRoleRepository.existsByMemberAndRole(member, Role.ADMIN),
        ).thenReturn(false)

        assertThrows<NotAuthorizedException> {
            postService.updatePost(1L, 2L, request)
        }
    }

    @Test
    fun `게시글 삭제 성공 테스트`() {
        val post = createPostTest()
        `when`(postRepository.findById(1L)).thenReturn(Optional.of(post))

        postService.deletePost(1L, 1L)

        verify(postRepository, times(1)).delete(post)
    }
}
