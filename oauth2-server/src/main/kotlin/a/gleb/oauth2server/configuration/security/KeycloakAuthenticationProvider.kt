package a.gleb.oauth2server.configuration.security

import a.gleb.oauth2server.db.entity.KeycloakAuthorizationSession
import a.gleb.oauth2server.db.repository.KeycloakAuthorizationSessionRepository
import a.gleb.oauth2server.service.KeycloakAuthorizationService
import org.apache.logging.log4j.util.Strings
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.core.OAuth2AccessToken
import java.time.LocalDateTime
import java.time.ZoneId

const val SESSION_STATE = "__session_state__"
const val CHAR_SEPARATOR = ','

class KeycloakAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val keycloakAuthorizationService: KeycloakAuthorizationService,
    private val keycloakAuthorizationSessionRepository: KeycloakAuthorizationSessionRepository
) : AbstractUserDetailsAuthenticationProvider() {

    override fun additionalAuthenticationChecks(
        userDetails: UserDetails,
        authentication: UsernamePasswordAuthenticationToken?
    ) {
        val authorize: OAuth2AccessToken?

        try {
            authorize =
                keycloakAuthorizationService.authorize(userDetails.username, authentication?.credentials.toString())
        } catch (exception: Exception) {
            logger.error { "Error while authorize in Keycloak. [username=${userDetails.username}], ${exception.message}" }
            throw BadCredentialsException(
                this.messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad Credentials"
                )
            )
        }

        validateAuthorizeToken(authorize)
        val session = KeycloakAuthorizationSession(
            null,
            SESSION_STATE,
            Strings.join(authorize.scopes, CHAR_SEPARATOR),
            LocalDateTime.ofInstant(authorize.expiresAt, ZoneId.systemDefault()),
            userDetails.username,
            LocalDateTime.now()
        )

        keycloakAuthorizationSessionRepository.save(session)
    }

    override fun retrieveUser(username: String?, authentication: UsernamePasswordAuthenticationToken?): UserDetails {
        return userDetailsService.loadUserByUsername(username)
    }

    private fun validateAuthorizeToken(oAuth2AccessToken: OAuth2AccessToken?) {
        if (oAuth2AccessToken == null || oAuth2AccessToken.tokenValue == null) {
            logger.error { "Error while validate token from Keycloak." }
            throw BadCredentialsException(
                this.messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"
                )
            )
        }
    }
}