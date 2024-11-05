package com.jungle.kotlinboard.common.config

import com.jungle.kotlinboard.service.MemberService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.Optional

@Configuration
@EnableJpaAuditing
class JpaConfig(
    private val memberService: MemberService,
) {
    @Bean
    fun auditorAware(): AuditorAware<String> =
        AuditorAware {
            Optional.of(memberService.getCurrentName())
        }
}
