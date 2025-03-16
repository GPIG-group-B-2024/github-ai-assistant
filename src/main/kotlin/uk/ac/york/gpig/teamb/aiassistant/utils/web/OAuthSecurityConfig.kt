package uk.ac.york.gpig.teamb.aiassistant.utils.web

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.web.server.ServerHttpSecurity.http
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@Configuration
@EnableWebSecurity
class OAuthSecurityConfig(
    @Value("\${okta.oauth2.issuer}")
    private val issuer: String,
    @Value("\${okta.oauth2.client-id}")
    private val clientId: String,
) {
    /**
     * Set up security so that:
     *  - Any authenticated user can see the admin dashboard (read-only)
     *  - No authentication is required for the webhook endpoint (will handle separately)
     * */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/css/**", permitAll)
                authorize("/webhooks", permitAll)
                authorize("/admin/**", authenticated)
            }
            oauth2Login { }
            logout {
                addLogoutHandler(logoutHandler)
            }
        }
        return http.build()
    }

    private val logoutHandler =
        LogoutHandler { req, resp, auth ->
            val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
            resp.sendRedirect("${issuer}v2/logout?client_id=$clientId&returnTo=$baseUrl")
        }
}
