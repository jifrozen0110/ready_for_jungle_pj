package com.jungle.kotlinboard.common.support

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder

fun MockMvc.performWithCsrf(requestBuilder: MockHttpServletRequestBuilder): ResultActions = this.perform(requestBuilder.with(csrf()))
