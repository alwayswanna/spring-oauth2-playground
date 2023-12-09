package a.gleb.oauth2server.utils

import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties.Client
import org.springframework.boot.context.properties.PropertyMapper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import java.time.Duration
import java.util.*

fun build(
    it: Map.Entry<String, Client>,
    passwordEncoder: PasswordEncoder
): RegisteredClient {
    val registration = it.value.registration
    val map = PropertyMapper.get().alwaysApplyingWhenNonNull()
    val builder = RegisteredClient.withId(it.key)
    map.from { registration.clientId }.to { clientId: String -> builder.clientId(clientId) }
    map.from { registration.clientSecret }.to { secret: String -> builder.clientSecret(passwordEncoder.encode(secret)) }
    map.from { registration.clientName }.to { clientName: String -> builder.clientName(clientName) }

    registration.clientAuthenticationMethods
        .asSequence()
        .map { ClientAuthenticationMethod(it) }
        .forEach {
            builder.clientAuthenticationMethod(it)
        }

    registration.authorizationGrantTypes
        .asSequence()
        .map { AuthorizationGrantType(it) }
        .forEach {
            builder.authorizationGrantType(it)
        }

    registration.redirectUris.forEach {
        builder.redirectUri(it)
    }

    registration.postLogoutRedirectUris
        .forEach {
            builder.postLogoutRedirectUri(it)
        }

    registration.scopes.forEach {
        builder.scope(it)
    }

    builder.clientSettings(getClientSettings(it.value, map))
    builder.tokenSettings(getTokenSettings(it.value, map))
    return builder.build()
}

private fun getClientSettings(client: Client, map: PropertyMapper): ClientSettings {
    val builder = ClientSettings.builder()
    map.from { client.isRequireProofKey }.to { requireProofKey: Boolean -> builder.requireProofKey(requireProofKey) }
    map.from { client.isRequireAuthorizationConsent }
        .to { requireAuthorizationConsent: Boolean -> builder.requireAuthorizationConsent(requireAuthorizationConsent) }
    map.from { client.jwkSetUri }.to { jwkSetUrl: String -> builder.jwkSetUrl(jwkSetUrl) }
    map.from { client.tokenEndpointAuthenticationSigningAlgorithm }
        .`as` { jwsAlgorithm(it) } to builder::tokenEndpointAuthenticationSigningAlgorithm
    return builder.build()
}

private fun getTokenSettings(client: Client, map: PropertyMapper): TokenSettings {
    val token = client.token
    val builder = TokenSettings.builder()
    map.from { token.authorizationCodeTimeToLive }
        .to { authorizationCodeTimeToLive: Duration -> builder.authorizationCodeTimeToLive(authorizationCodeTimeToLive) }
    map.from { token.accessTokenTimeToLive }
        .to { accessTokenTimeToLive: Duration -> builder.accessTokenTimeToLive(accessTokenTimeToLive) }
    map.from { token.accessTokenFormat }.`as` { value: String -> OAuth2TokenFormat(value) }
        .to { accessTokenFormat: OAuth2TokenFormat -> builder.accessTokenFormat(accessTokenFormat) }
    map.from { token.deviceCodeTimeToLive }
        .to { deviceCodeTimeToLive: Duration -> builder.deviceCodeTimeToLive(deviceCodeTimeToLive) }
    map.from { token.isReuseRefreshTokens }
        .to { reuseRefreshTokens: Boolean -> builder.reuseRefreshTokens(reuseRefreshTokens) }
    map.from { token.refreshTokenTimeToLive }
        .to { refreshTokenTimeToLive: Duration -> builder.refreshTokenTimeToLive(refreshTokenTimeToLive) }
    map.from { token.idTokenSignatureAlgorithm }
        .`as` { signatureAlgorithm(it) } to builder::idTokenSignatureAlgorithm
    return builder.build()
}

private fun jwsAlgorithm(signingAlgorithm: String): JwsAlgorithm {
    val name = signingAlgorithm.uppercase(Locale.getDefault())
    return SignatureAlgorithm.from(name)
}

private fun signatureAlgorithm(signatureAlgorithm: String): SignatureAlgorithm {
    return SignatureAlgorithm.from(signatureAlgorithm.uppercase(Locale.getDefault()))
}