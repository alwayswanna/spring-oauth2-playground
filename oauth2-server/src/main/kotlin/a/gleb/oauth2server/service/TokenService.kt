package a.gleb.oauth2server.service

import a.gleb.oauth2server.db.repository.OAuth2AuthorizationInnerRepository
import a.gleb.oauth2server.logger
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.stereotype.Service

const val PREFERRED_USERNAME = "preferred_username"

@Service
class TokenService(
    private val oAuth2AuthorizationService: OAuth2AuthorizationService,
    private val oAuth2AuthorizationInnerRepository: OAuth2AuthorizationInnerRepository
) {

    fun offer(): Map<String, String> {
        logger.info { "Start offer token, from Keycloak" }
        val context = SecurityContextHolder.getContext()
        val jwt = context.authentication.principal as Jwt
        val claims = jwt.claims

        val username = claims[PREFERRED_USERNAME]
            ?: throw IllegalStateException("Token does not contains $PREFERRED_USERNAME")

        logger.info { "User has authorization from Keycloak JWT. [preferred_username=$username]" }

        val session = oAuth2AuthorizationInnerRepository.findByPrincipalName(username as String)
            ?: throw IllegalStateException("Session not founded")

        logger.info { "Current user session. [session_id=${session.id}]" }

        val authorization = oAuth2AuthorizationService.findById(session.id)

        if (authorization != null && authorization.refreshToken != null && authorization.refreshToken!!.token != null) {
            return mapOf(
                OAuth2ParameterNames.REFRESH_TOKEN to authorization.refreshToken!!.token.tokenValue
            )
        } else {
            logger.error { "Error while get refresh token from session. [session_id=${session.id}] " }
            throw UsernameNotFoundException("Not found session for $username")
        }
    }
}