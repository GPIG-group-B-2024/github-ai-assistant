package uk.ac.york.gpig.teamb.aiassistant.mockMvc

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.LLMConversationManager
import uk.ac.york.gpig.teamb.aiassistant.database.llmConversation.entities.LLMConversationEntity
import uk.ac.york.gpig.teamb.aiassistant.utils.web.DashboardAuthorityMapper
import uk.ac.york.gpig.teamb.aiassistant.utils.web.OAuthSecurityConfig
import java.time.Instant

@AutoConfigureMockMvc
@ContextConfiguration(classes = [OAuthSecurityConfig::class])
class ConversationAdminControllerMockMvcTest {
    @MockkBean(relaxed = true)
    private lateinit var llmConversationManager: LLMConversationManager

    @MockkBean(relaxed = true)
    private lateinit var dashboardAuthorityMapper: DashboardAuthorityMapper

    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var context: WebApplicationContext

    @BeforeEach
    fun setup() {
        mockMvc =
            MockMvcBuilders
                .webAppContextSetup(context)
                .apply<DefaultMockMvcBuilder>(springSecurity())
                .build()
    }

    @Test
    fun `allows viewing the dashboard when user is registered at the uni of york`() {
        every { llmConversationManager.fetchConversations() } returns emptyList<LLMConversationEntity>()
        val token =
            OidcIdToken(
                "mock-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                mapOf(
                    "sub" to "12345",
                    "email" to "my-user@york.ac.uk",
                ),
            )
        val user = DefaultOidcUser(emptyList<SimpleGrantedAuthority>(), token)
        mockMvc.perform(
            get("/admin/conversations")
                .with(oidcLogin().oidcUser(user)),
        ).andExpect(
            status().isOk,
        )
        verify {
            llmConversationManager.fetchConversations()
        }
    }

    @Test
    @WithMockUser
    fun `blocks users from non-uni addresses`() {
        every { llmConversationManager.fetchConversations() } returns emptyList<LLMConversationEntity>()
        val token =
            OidcIdToken(
                "mock-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                mapOf(
                    "sub" to "12345",
                    "email" to "my-user@funky-domain.com",
                ),
            )
        val user = DefaultOidcUser(emptyList<SimpleGrantedAuthority>(), token)
        mockMvc.perform(
            get("/admin/conversations")
                .with(oidcLogin().oidcUser(user)),
        ).andExpect(
            status().isForbidden,
        )
        verify(exactly = 0) {
            llmConversationManager.fetchConversations()
        }
    }
}
