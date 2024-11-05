package com.jungle.kotlinboard.controller

import com.jungle.kotlinboard.common.utils.SecurityUtils
import com.jungle.kotlinboard.domain.Post
import com.jungle.kotlinboard.service.PostCreateRequest
import com.jungle.kotlinboard.service.PostResponse
import com.jungle.kotlinboard.service.PostService
import com.jungle.kotlinboard.service.PostUpdateRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService,
) {
    @PostMapping()
    fun createPost(
        @RequestBody postCreateRequest: PostCreateRequest,
    ): PostResponse {
        val id = SecurityUtils.userId()

        return postService.insertPost(id, postCreateRequest)
    }

    @GetMapping("/{postId}")
    fun getPost(
        @PathVariable postId: Long,
    ): PostResponse = postService.getPost(postId)

    @GetMapping("")
    fun getPosts(): List<Post> = postService.getPosts()

    @PatchMapping("/{postId}")
    fun updatePost(
        @PathVariable postId: Long,
        @RequestBody postUpdateRequest: PostUpdateRequest,
    ) {
        val id = SecurityUtils.userId()

        postService.updatePost(postId, id, postUpdateRequest)
    }

    @DeleteMapping("/{postId}")
    fun deletePost(
        @PathVariable postId: Long,
    ) {
        val id = SecurityUtils.userId()
        postService.deletePost(postId, id)
    }
}
