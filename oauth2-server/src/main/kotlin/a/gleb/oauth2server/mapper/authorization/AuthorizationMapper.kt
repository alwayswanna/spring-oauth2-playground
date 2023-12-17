package a.gleb.oauth2server.mapper.authorization

import a.gleb.oauth2server.constants.COMMA
import a.gleb.oauth2server.constants.EMPTY
import a.gleb.oauth2server.db.entity.authorization.Authorization
import a.gleb.oauth2server.db.entity.authorization.OAuth2AuthorizationEntityBuilder
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.core.AuthorizationGrantType.*
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.STATE
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.function.Consumer


@Component
class AuthorizationMapper {

    private final var objectMapper: ObjectMapper

    init {
        val classLoader = this.javaClass.classLoader
        val securityModules = SecurityJackson2Modules.getModules(classLoader)
        objectMapper = ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerModule(OAuth2AuthorizationServerJackson2Module())
            .registerModules(securityModules)
    }

    /**
     * Method map to database builder. [OAuth2Authorization] -> [Authorization].
     */
    fun toOAuth2Authorization(authorization: OAuth2Authorization): Authorization {
        val builder = OAuth2AuthorizationEntityBuilder()
            .id(authorization.id)
            .registeredClientId(authorization.registeredClientId)
            .principalName(authorization.principalName)
            .authorizationGrantType(authorization.authorizationGrantType.value)
            .authorizedScopes(authorization.authorizedScopes.joinToString(COMMA))
            .attributes(writeMap(authorization.attributes))
            .state(authorization.attributes[STATE].toString())


        val authorizationCode = authorization.getToken(OAuth2AuthorizationCode::class.java)
        setTokenValues(
            authorizationCode,
            builder::authorizationCodeValue,
            builder::authorizationCodeIssuedAt,
            builder::authorizationCodeExpiresAt,
            builder::authorizationCodeMetadata
        )

        val accessToken = authorization.getToken(OAuth2AccessToken::class.java)
        setTokenValues(
            accessToken,
            builder::accessTokenValue,
            builder::accessTokenIssuedAt,
            builder::accessTokenExpiresAt,
            builder::accessTokenMetadata
        )
        builder.accessTokenType(accessToken?.token?.tokenType?.value)

        if (accessToken != null && !accessToken.token.scopes.isNullOrEmpty()) {
            builder.accessTokenScopes(accessToken.token.scopes.joinToString(COMMA))
        }

        val refreshToken = authorization.getToken(OAuth2RefreshToken::class.java)
        setTokenValues(
            refreshToken,
            builder::refreshTokenValue,
            builder::refreshTokenIssuedAt,
            builder::refreshTokenExpiresAt,
            builder::refreshTokenMetadata
        )

        val oidcIdToken = authorization.getToken(OidcIdToken::class.java)
        setTokenValues(
            oidcIdToken,
            builder::oidcIdTokenValue,
            builder::oidcIdTokenIssuedAt,
            builder::oidcIdTokenExpiresAt,
            builder::oidcIdTokenMetadata
        )

        if (oidcIdToken != null) {
            builder.oidcIdTokenClaims(writeMap(oidcIdToken.claims!!))
        }

        val userCode = authorization.getToken(OAuth2UserCode::class.java)
        setTokenValues(
            userCode,
            builder::userCodeValue,
            builder::userCodeIssuedAt,
            builder::userCodeExpiresAt,
            builder::userCodeMetadata
        )

        val deviceCode = authorization.getToken(OAuth2DeviceCode::class.java)
        setTokenValues(
            deviceCode,
            builder::deviceCodeValue,
            builder::deviceCodeIssuedAt,
            builder::deviceCodeExpiresAt,
            builder::deviceCodeMetadata
        )

        return builder.build()
    }

    /**
     * Method map to security dto. [Authorization] -> [OAuth2Authorization].
     */
    fun toOAuth2Authorization(
        authorization: Authorization,
        registeredClient: RegisteredClient
    ): OAuth2Authorization? {

        val oAuth2Authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
            .id(authorization.id.toString())
            .principalName(authorization.principalName)
            .authorizationGrantType(parseAuthorizationGrantTypes(authorization.authorizationGrantType))
            .authorizedScopes(authorization.authorizedScopes?.split(COMMA)?.toSet())
            .attributes { it.putAll(parseMap(authorization.attributes)) }

        oAuth2Authorization.attribute(STATE, authorization.state)

        if (!authorization.authorizationCodeValue.isNullOrEmpty()) {
            val authorizationCode = OAuth2AuthorizationCode(
                authorization.authorizationCodeValue,
                authorization.authorizationCodeIssuedAt,
                authorization.authorizationCodeExpiresAt
            )
            oAuth2Authorization.token(authorizationCode) { metadata ->
                metadata.putAll(parseMap(authorization.authorizationCodeMetadata ?: EMPTY))
            }
        }

        if (!authorization.accessTokenValue.isNullOrEmpty()) {
            val accessToken = OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                authorization.accessTokenValue,
                authorization.accessTokenIssuedAt,
                authorization.accessTokenExpiresAt,
                authorization.accessTokenScopes?.split(COMMA)?.toSet() ?: emptySet()
            )
            oAuth2Authorization.token(accessToken) { metadata ->
                metadata.putAll(
                    parseMap(
                        authorization.accessTokenMetadata ?: EMPTY
                    )
                )
            }
        }

        if (!authorization.refreshTokenValue.isNullOrEmpty()) {
            val refreshToken = OAuth2RefreshToken(
                authorization.refreshTokenValue,
                authorization.refreshTokenIssuedAt,
                authorization.refreshTokenExpiresAt
            )
            oAuth2Authorization.token(refreshToken) { metadata ->
                metadata.putAll(
                    parseMap(
                        authorization.refreshTokenMetadata ?: EMPTY
                    )
                )
            }
        }

        if (!authorization.oidcIdTokenValue.isNullOrEmpty()) {
            val idToken = OidcIdToken(
                authorization.oidcIdTokenValue,
                authorization.oidcIdTokenIssuedAt,
                authorization.oidcIdTokenExpiresAt,
                parseMap(authorization.oidcIdTokenClaims ?: EMPTY)
            )
            oAuth2Authorization.token(idToken) { metadata ->
                metadata.putAll(
                    parseMap(
                        authorization.oidcIdTokenMetadata ?: EMPTY
                    )
                )
            }
        }

        if (!authorization.userCodeValue.isNullOrEmpty()) {
            val userCode = OAuth2UserCode(
                authorization.userCodeValue,
                authorization.userCodeIssuedAt,
                authorization.userCodeExpiresAt
            )
            oAuth2Authorization.token(userCode) { metadata ->
                metadata.putAll(
                    parseMap(
                        authorization.userCodeMetadata ?: EMPTY
                    )
                )
            }
        }

        if (!authorization.deviceCodeValue.isNullOrEmpty()) {
            val deviceCode = OAuth2DeviceCode(
                authorization.deviceCodeValue,
                authorization.deviceCodeIssuedAt,
                authorization.deviceCodeExpiresAt
            )
            oAuth2Authorization.token(deviceCode) { metadata ->
                metadata.putAll(
                    parseMap(
                        authorization.deviceCodeMetadata ?: EMPTY
                    )
                )
            }
        }

        return oAuth2Authorization.build()
    }

    fun parseAuthorizationGrantTypes(grantTypesString: String): AuthorizationGrantType {
        return when (grantTypesString) {
            AUTHORIZATION_CODE.value -> AUTHORIZATION_CODE
            CLIENT_CREDENTIALS.value -> CLIENT_CREDENTIALS
            REFRESH_TOKEN.value -> REFRESH_TOKEN
            DEVICE_CODE.value -> DEVICE_CODE
            else -> {
                AuthorizationGrantType(grantTypesString)
            }
        }
    }

    private fun parseMap(data: String?): Map<String, Any> {
        try {
            if (data.isNullOrEmpty()) {
                return emptyMap()
            }
            return objectMapper.readValue(data, object : TypeReference<Map<String, Any>>() {})
        } catch (ex: Exception) {
            throw IllegalArgumentException(ex.message, ex)
        }
    }

    private fun writeMap(metadata: Map<String, Any>): String {
        try {
            return objectMapper.writeValueAsString(metadata)
        } catch (ex: Exception) {
            throw IllegalArgumentException(ex.message, ex)
        }
    }

    private fun setTokenValues(
        token: OAuth2Authorization.Token<*>?,
        tokenValueConsumer: Consumer<String>,
        issuedAtConsumer: Consumer<Instant>,
        expiresAtConsumer: Consumer<Instant>,
        metadataConsumer: Consumer<String>
    ) {
        if (token != null) {
            val oAuth2Token = token.token
            tokenValueConsumer.accept(oAuth2Token.tokenValue)
            issuedAtConsumer.accept(oAuth2Token.issuedAt!!)
            expiresAtConsumer.accept(oAuth2Token.expiresAt!!)
            metadataConsumer.accept(writeMap(token.metadata))
        }
    }
}