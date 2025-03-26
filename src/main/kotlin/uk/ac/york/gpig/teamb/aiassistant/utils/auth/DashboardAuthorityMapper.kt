package uk.ac.york.gpig.teamb.aiassistant.utils.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component

@Component
class DashboardAuthorityMapper(
    @Value("\${app_settings.auth0_groups-claim}")
    private val groupsClaim: String,
) : OidcUserService() {
    override fun loadUser(userRequest: OidcUserRequest?): OidcUser? {
        val oidcUser: OAuth2User = super.loadUser(userRequest)
        val oldAuthorities = oidcUser.authorities

        @Suppress("UNCHECKED_CAST") // not ideal, but we know the format of this is consistent
        val assignedRoles = (oidcUser.attributes[groupsClaim] as List<String>).map { SimpleGrantedAuthority(it) }
        val email = oidcUser.attributes["email"] as String?
        val additionalPermissions = mutableListOf<SimpleGrantedAuthority>()
        // Grant the dashboard permissions to admins, guests, and any york students
        if (email != null && email.endsWith("@york.ac.uk") ||
            assignedRoles.any { it.authority == "ROLE_ADMIN" || it.authority == "ROLE_GUEST" }
        ) {
            additionalPermissions.add(SimpleGrantedAuthority("dashboard:view"))
        }

        return DefaultOidcUser(
            additionalPermissions + oldAuthorities,
            userRequest!!.idToken,
        )
    }
}
