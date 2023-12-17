package a.gleb.oauth2server.service.authorization.handler

import a.gleb.oauth2server.service.UserStoreService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class FederationIdentityAuthenticationSuccessHandler(
    private val oauth2UserHandler: UserStoreService
) : AuthenticationSuccessHandler {

    private val delegate: AuthenticationSuccessHandler = SavedRequestAwareAuthenticationSuccessHandler()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        if (authentication is OAuth2AuthenticationToken) {
            if (authentication.principal is OidcUser) {
                this.oauth2UserHandler.accept(authentication.principal as OidcUser)
            } else if (authentication.principal is OAuth2User) {
                this.oauth2UserHandler.accept(authentication.principal)
            }
        }

        this.delegate.onAuthenticationSuccess(request, response, authentication)
    }
}