package a.gleb.oauth2server.mapper.authorization

import a.gleb.oauth2server.constants.COMMA
import a.gleb.oauth2server.db.entity.authorization.Client
import a.gleb.oauth2server.model.RegisteredClientRequest
import a.gleb.oauth2server.model.RegisteredClientResponse
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames.Client.REQUIRE_AUTHORIZATION_CONSENT
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames.Client.REQUIRE_PROOF_KEY
import org.springframework.security.oauth2.server.authorization.settings.ConfigurationSettingNames.Token.*
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.util.*
/**
 * [Client] entity mapper.
 * Map entity to response DTO [RegisteredClientResponse];
 * Map request DTO [RegisteredClientRequest] to entity;
 * Map Spring OAuth2 Client DTO [RegisteredClient] to entity;
 * Map entity to Spring OAuth2 Client DTO;
 */
@Component
class ClientMapper {

    fun toRegisteredClient(client: Client?): RegisteredClient? {
        if (client == null) {
            return null
        }

        val clientAuthenticationMethods = client.clientAuthenticationMethods.split(COMMA).asSequence()
            .map { resolveClientAuthenticationMethod(it) }
            .toSet()

        val authorizationGrantTypes = client.authorizationGrantTypes.split(COMMA).asSequence()
            .map { resolveAuthorizationGrantType(it) }
            .toSet()

        return RegisteredClient.withId(client.id.toString())
            .clientId(client.clientId)
            .clientIdIssuedAt(client.clientIdIssuedAt)
            .clientSecret(client.clientSecret)
            .clientSecretExpiresAt(client.clientSecretExpiresAt)
            .clientName(client.clientName)
            .authorizationGrantTypes { it.addAll(authorizationGrantTypes) }
            .clientAuthenticationMethods { it.addAll(clientAuthenticationMethods) }
            .redirectUris { it.addAll(client.redirectUris?.split(COMMA) ?: listOf()) }
            .postLogoutRedirectUris { it.addAll(client.postLogoutRedirectUris?.split(COMMA) ?: listOf()) }
            .scopes { it.addAll(client.scopes.split(COMMA)) }
            .clientSettings(
                ClientSettings.builder()
                    .requireProofKey(client.clientSettings[REQUIRE_PROOF_KEY] as Boolean)
                    .requireAuthorizationConsent(client.clientSettings[REQUIRE_AUTHORIZATION_CONSENT] as Boolean)
                    .build()
            )
            .tokenSettings(
                TokenSettings.builder()
                    .reuseRefreshTokens(client.tokenSettings[REUSE_REFRESH_TOKENS] as Boolean)
                    .idTokenSignatureAlgorithm(SignatureAlgorithm.valueOf(client.tokenSettings[ID_TOKEN_SIGNATURE_ALGORITHM] as String))
                    .accessTokenTimeToLive(Duration.ofSeconds((client.tokenSettings[ACCESS_TOKEN_TIME_TO_LIVE] as Double).toLong()))
                    .accessTokenFormat(OAuth2TokenFormat((client.tokenSettings[ACCESS_TOKEN_FORMAT] as Map<*, *>)["value"].toString()))
                    .refreshTokenTimeToLive(Duration.ofSeconds((client.tokenSettings[REFRESH_TOKEN_TIME_TO_LIVE] as Double).toLong()))
                    .authorizationCodeTimeToLive(Duration.ofSeconds((client.tokenSettings[AUTHORIZATION_CODE_TIME_TO_LIVE] as Double).toLong()))
                    .deviceCodeTimeToLive(Duration.ofSeconds((client.tokenSettings[DEVICE_CODE_TIME_TO_LIVE] as Double).toLong()))
                    .build()
            )
            .build()

    }

    fun toEntity(registeredClient: RegisteredClient): Client {
        val clientAuthMethods = registeredClient.clientAuthenticationMethods.asSequence()
            .map { it.value }
            .joinToString(COMMA)

        val grantTypes = registeredClient.authorizationGrantTypes.asSequence()
            .map { it.value }
            .joinToString(COMMA)

        return Client(
            registeredClient.id,
            registeredClient.clientId,
            registeredClient.clientIdIssuedAt?: Instant.now(),
            registeredClient.clientSecret,
            registeredClient.clientSecretExpiresAt,
            registeredClient.clientName,
            clientAuthMethods,
            grantTypes,
            registeredClient.redirectUris.joinToString(COMMA),
            registeredClient.postLogoutRedirectUris.joinToString(COMMA),
            registeredClient.scopes.joinToString(COMMA),
            registeredClient.clientSettings.settings,
            registeredClient.tokenSettings.settings
        )
    }

    fun toResponse(entity: Client): RegisteredClientResponse {
        return RegisteredClientResponse(
            UUID.fromString(entity.id),
            entity.clientId,
            entity.clientSecret!!,
            entity.authorizationGrantTypes.splitToSequence(COMMA).toList(),
            entity.clientAuthenticationMethods.splitToSequence(COMMA).toList(),
            entity.redirectUris?.splitToSequence(COMMA)?.toList() ?: emptyList(),
            entity.postLogoutRedirectUris?.splitToSequence(COMMA)?.toList() ?: emptyList(),
            entity.scopes.splitToSequence(COMMA).toList()
        )
    }

    private fun resolveAuthorizationGrantType(authorizationGrantType: String): AuthorizationGrantType {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.value == authorizationGrantType) {
            return AuthorizationGrantType.AUTHORIZATION_CODE
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.value == authorizationGrantType) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS
        } else if (AuthorizationGrantType.REFRESH_TOKEN.value == authorizationGrantType) {
            return AuthorizationGrantType.REFRESH_TOKEN
        }
        return AuthorizationGrantType(authorizationGrantType) // Custom authorization grant type
    }

    private fun resolveClientAuthenticationMethod(clientAuthenticationMethod: String): ClientAuthenticationMethod {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.value == clientAuthenticationMethod) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.value == clientAuthenticationMethod) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST
        } else if (ClientAuthenticationMethod.NONE.value == clientAuthenticationMethod) {
            return ClientAuthenticationMethod.NONE
        }
        return ClientAuthenticationMethod(clientAuthenticationMethod) // Custom client authentication method
    }
}