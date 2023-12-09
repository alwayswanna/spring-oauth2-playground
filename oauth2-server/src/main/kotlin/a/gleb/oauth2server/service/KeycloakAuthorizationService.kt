package a.gleb.oauth2server.service

import a.gleb.oauth2server.logger
import jakarta.annotation.PostConstruct
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.stereotype.Service


const val REGISTRATION_ID = "keycloak"

@Service
class KeycloakAuthorizationService(
    private val oAuth2AuthorizedClientManager: AuthorizedClientServiceOAuth2AuthorizedClientManager
) {

    @PostConstruct
    fun afterPropertiesSet() {
        @Suppress("DEPRECATION")
        oAuth2AuthorizedClientManager.setAuthorizedClientProvider(
            OAuth2AuthorizedClientProviderBuilder.builder()
                .password()
                .refreshToken()
                .build()
        )
    }

    fun authorize(username: String, password: String): OAuth2AccessToken {
        logger.info { "Start login user in keycloak with username=$username." }

        oAuth2AuthorizedClientManager.setContextAttributesMapper { _: OAuth2AuthorizeRequest? ->
            mapOf(USERNAME_ATTRIBUTE_NAME to username, PASSWORD_ATTRIBUTE_NAME to password)
        }

        val request = OAuth2AuthorizeRequest.withClientRegistrationId(REGISTRATION_ID)
            .principal(REGISTRATION_ID)
            .build()

        val authResponse = oAuth2AuthorizedClientManager.authorize(request)

        logger.info { "Authorized in Keycloak, username=$username" }

        return authResponse?.accessToken
            ?: throw IllegalStateException(
                "Error while authorize on authorization server, [attributes=${request.attributes}]"
            )
    }
}