package a.gleb.oauth2server.mapper.authorization

import a.gleb.oauth2server.constants.COMMA
import a.gleb.oauth2server.constants.EMPTY
import a.gleb.oauth2server.db.entity.authorization.AuthorizationConsent
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.stereotype.Component

@Component
class AuthorizationConsentMapper {

    fun toAuthorizationConsent(authorizationConsent: OAuth2AuthorizationConsent): AuthorizationConsent {
        return AuthorizationConsent(
            registeredClientId = authorizationConsent.registeredClientId,
            principalName = authorizationConsent.principalName,
            authorities = authorizationConsent.authorities.joinToString(COMMA)
        )
    }

    fun toOAuth2AuthorizationConsent(
        authorizationConsent: AuthorizationConsent?,
        registeredClient: RegisteredClient
    ): OAuth2AuthorizationConsent? {
        if (authorizationConsent == null) {
            return null
        }

        return OAuth2AuthorizationConsent.withId(
            registeredClient.id,
            authorizationConsent.principalName ?: EMPTY
        )
            .authorities {
                it.addAll(authorizationConsent.authorities
                    ?.split(COMMA)
                    ?.map { auth -> SimpleGrantedAuthority(auth) }
                    ?.toSet() ?: emptySet())
            }
            .build()


    }
}