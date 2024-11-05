package com.jungle.kotlinboard.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.jungle.kotlinboard.common.auth.WithMockCustomUser
import com.jungle.kotlinboard.common.fixture.TEST_USER_ID
import com.jungle.kotlinboard.common.support.performWithCsrf
import com.jungle.kotlinboard.domain.Post
import com.jungle.kotlinboard.service.PostCreateRequest
import com.jungle.kotlinboard.service.PostResponse
import com.jungle.kotlinboard.service.PostService
import com.jungle.kotlinboard.service.PostUpdateRequest
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(PostController::class)
class PostControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var postService: PostService

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    @WithMockCustomUser
    fun `게시글 생성 테스트`() {
        // given
        val request =
            PostCreateRequest(
                title = "test title",
                content = "test content",
            )
        val response =
            PostResponse(
                id = 1L,
                title = request.title,
                content = request.content,
                createdDate = LocalDateTime.now(),
                createdBy = "test",
            )

        given(postService.insertPost(TEST_USER_ID, request)).willReturn(response)

        // when & then
        mockMvc
            .performWithCsrf(
                post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(response.id))
            .andExpect(jsonPath("$.title").value(response.title))
            .andExpect(jsonPath("$.content").value(response.content))
    }

    @Test
    @WithMockCustomUser
    fun `게시글 조회 테스트`() {
        // given
        val postId = 1L
        val response =
            PostResponse(
                id = postId,
                title = "test title",
                content = "test content",
                createdDate = LocalDateTime.now(),
                createdBy = "test",
            )

        given(postService.getPost(postId)).willReturn(response)

        // when & then
        mockMvc
            .perform(get("/api/posts/$postId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(response.id))
            .andExpect(jsonPath("$.title").value(response.title))
            .andExpect(jsonPath("$.content").value(response.content))
    }

    @Test
    @WithMockCustomUser
    fun `게시글 목록 조회 테스트`() {
        // given
        val posts =
            listOf(
                Post(1L, "title1", "content1", 1L),
                Post(2L, "title2", "content2", 1L),
            )

        given(postService.getPosts()).willReturn(posts)

        // when & then
        mockMvc
            .perform(get("/api/posts"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(posts.size))
    }

    @Test
    @WithMockCustomUser
    fun `게시글 수정 테스트`() {
        // given
        val postId = 1L
        val updateRequest =
            PostUpdateRequest(
                title = "updated title",
                content = "updated content",
            )

        willDoNothing().given(postService).updatePost(postId, TEST_USER_ID, updateRequest)

        // when & then
        mockMvc
            .performWithCsrf(
                patch("/api/posts/$postId")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)),
            ).andExpect(status().isOk)
    }

    @Test
    @WithMockCustomUser
    fun `게시글 삭제 테스트`() {
        // given
        val postId = 1L

        willDoNothing().given(postService).deletePost(postId, 1L)

        // when & then
        mockMvc
            .performWithCsrf(delete("/api/posts/$postId"))
            .andExpect(status().isOk)
    }
}
