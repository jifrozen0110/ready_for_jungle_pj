package com.jungle.kotlinboard.common.fixture

import com.jungle.kotlinboard.domain.Post
import com.jungle.kotlinboard.service.PostCreateRequest
import com.jungle.kotlinboard.service.PostResponse
import com.jungle.kotlinboard.service.PostUpdateRequest
import java.time.LocalDateTime

const val POST_TEST_TITLE = "게시글 제목 테스트"
const val POST_TEST_CONTENT = "게시글 본문 테스트"

const val POST_TEST_UPDATE_TITLE = "게시글 제목 수정 테스트"
const val TEST_USER_ID = 1L

fun createPost(): Post = Post.of(POST_TEST_TITLE, POST_TEST_CONTENT, TEST_USER_ID)

fun createPostCreateRequest(): PostCreateRequest = PostCreateRequest(POST_TEST_TITLE, POST_TEST_CONTENT)

fun createPostUpdateRequest(
    title: String? = null,
    content: String? = null,
): PostUpdateRequest =
    PostUpdateRequest(
        title = title,
        content = content,
    )

fun createPostResponse(): PostResponse =
    PostResponse(
        1L,
        POST_TEST_TITLE,
        POST_TEST_CONTENT,
        LocalDateTime.now(),
        "test",
    )

fun createPostTest(): Post = Post(id = 1L, POST_TEST_TITLE, POST_TEST_CONTENT, TEST_USER_ID)
