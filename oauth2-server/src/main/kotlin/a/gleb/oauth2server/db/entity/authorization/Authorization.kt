package a.gleb.oauth2server.db.entity.authorization

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant

@Entity
@Table(name = "oauth2_authorization")
data class Authorization(

    @Id
    @Column(name = "id")
    var id: String,

    @Column(name = "registered_client_id")
    var registeredClientId: String,

    @Column(name = "principal_name")
    var principalName: String,

    @Column(name = "authorization_grant_type")
    var authorizationGrantType: String,

    @Column(name = "authorized_scopes", length = 1000)
    var authorizedScopes: String?,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", length = 4000, columnDefinition = "jsonb")
    var attributes: String?,

    @Column(name = "state", length = 500)
    var state: String?,

    @Column(name = "authorization_code_value", length = 4000)
    var authorizationCodeValue: String?,

    @Column(name = "authorization_code_issued_at")
    var authorizationCodeIssuedAt: Instant?,

    @Column(name = "authorization_code_expires_at")
    var authorizationCodeExpiresAt: Instant?,

    @Column(name = "authorization_code_metadata")
    var authorizationCodeMetadata: String?,

    @Column(name = "access_token_value", length = 4000)
    var accessTokenValue: String?,

    @Column(name = "access_token_issued_at")
    var accessTokenIssuedAt: Instant?,

    @Column(name = "access_token_expires_at")
    var accessTokenExpiresAt: Instant?,

    @Column(name = "access_token_metadata", length = 2000)
    var accessTokenMetadata: String?,

    @Column(name = "access_token_type")
    var accessTokenType: String?,

    @Column(name = "access_token_scopes", length = 1000)
    var accessTokenScopes: String?,

    @Column(name = "refresh_token_value", length = 4000)
    var refreshTokenValue: String?,

    @Column(name = "refresh_token_issued_at")
    var refreshTokenIssuedAt: Instant?,

    @Column(name = "refresh_token_expires_at")
    var refreshTokenExpiresAt: Instant?,

    @Column(name = "refresh_token_metadata", length = 2000)
    var refreshTokenMetadata: String?,

    @Column(name = "oidc_id_token_value", length = 4000)
    var oidcIdTokenValue: String?,

    @Column(name = "oidc_id_token_issued_at")
    var oidcIdTokenIssuedAt: Instant?,

    @Column(name = "oidc_id_token_expires_at")
    var oidcIdTokenExpiresAt: Instant?,

    @Column(name = "oidc_id_token_metadata", length = 2000)
    var oidcIdTokenMetadata: String?,

    @Column(name = "oidc_id_token_claims", length = 2000)
    var oidcIdTokenClaims: String?,

    @Column(name = "user_code_value", length = 4000)
    var userCodeValue: String?,

    @Column(name = "user_code_issued_at")
    var userCodeIssuedAt: Instant?,

    @Column(name = "user_code_expires_at")
    var userCodeExpiresAt: Instant?,

    @Column(name = "user_code_metadata", length = 2000)
    var userCodeMetadata: String?,

    @Column(name = "device_code_value", length = 4000)
    var deviceCodeValue: String?,

    @Column(name = "device_code_issued_at")
    var deviceCodeIssuedAt: Instant?,

    @Column(name = "device_code_expires_at")
    var deviceCodeExpiresAt: Instant?,

    @Column(name = "device_code_metadata", length = 2000)
    var deviceCodeMetadata: String?


)

data class OAuth2AuthorizationEntityBuilder(
    var id: String = "",
    var registeredClientId: String = "",
    var principalName: String = "",
    var authorizationGrantType: String = "",
    var authorizedScopes: String? = null,
    var attributes: String? = null,
    var state: String? = null,
    var authorizationCodeValue: String? = null,
    var authorizationCodeIssuedAt: Instant? = null,
    var authorizationCodeExpiresAt: Instant? = null,
    var authorizationCodeMetadata: String? = null,
    var accessTokenValue: String? = null,
    var accessTokenIssuedAt: Instant? = null,
    var accessTokenExpiresAt: Instant? = null,
    var accessTokenMetadata: String? = null,
    var accessTokenType: String? = null,
    var accessTokenScopes: String? = null,
    var refreshTokenValue: String? = null,
    var refreshTokenIssuedAt: Instant? = null,
    var refreshTokenExpiresAt: Instant? = null,
    var refreshTokenMetadata: String? = null,
    var oidcIdTokenValue: String? = null,
    var oidcIdTokenIssuedAt: Instant? = null,
    var oidcIdTokenExpiresAt: Instant? = null,
    var oidcIdTokenMetadata: String? = null,
    var oidcIdTokenClaims: String? = null,
    var userCodeValue: String? = null,
    var userCodeIssuedAt: Instant? = null,
    var userCodeExpiresAt: Instant? = null,
    var userCodeMetadata: String? = null,
    var deviceCodeValue: String? = null,
    var deviceCodeIssuedAt: Instant? = null,
    var deviceCodeExpiresAt: Instant? = null,
    var deviceCodeMetadata: String? = null
) {

    fun id(id: String) = apply {
        this.id = id
    }

    fun registeredClientId(registeredClientId: String) = apply {
        this.registeredClientId = registeredClientId
    }

    fun principalName(principalName: String) = apply {
        this.principalName = principalName
    }

    fun authorizationGrantType(authorizationGrantType: String) = apply {
        this.authorizationGrantType = authorizationGrantType
    }

    fun authorizedScopes(authorizedScopes: String) = apply {
        this.authorizedScopes = authorizedScopes
    }

    fun attributes(attributes: String) = apply {
        this.attributes = attributes
    }

    fun state(state: String) = apply {
        this.state = state
    }

    fun authorizationCodeValue(authorizationCodeValue: String?) = apply {
        this.authorizationCodeValue = authorizationCodeValue
    }

    fun authorizationCodeIssuedAt(authorizationCodeIssuedAt: Instant?) = apply {
        this.authorizationCodeIssuedAt = authorizationCodeIssuedAt
    }

    fun authorizationCodeExpiresAt(authorizationCodeExpiresAt: Instant?) = apply {
        this.authorizationCodeExpiresAt = authorizationCodeExpiresAt
    }

    fun authorizationCodeMetadata(authorizationCodeMetadata: String?) = apply {
        this.authorizationCodeMetadata = authorizationCodeMetadata
    }

    fun accessTokenValue(accessTokenValue: String?) = apply {
        this.accessTokenValue = accessTokenValue
    }

    fun accessTokenIssuedAt(accessTokenIssuedAt: Instant?) = apply {
        this.accessTokenIssuedAt = accessTokenIssuedAt
    }

    fun accessTokenExpiresAt(accessTokenExpiresAt: Instant?) = apply {
        this.accessTokenExpiresAt = accessTokenExpiresAt
    }

    fun accessTokenMetadata(accessTokenMetadata: String?) = apply {
        this.accessTokenMetadata = accessTokenMetadata
    }

    fun accessTokenType(accessTokenType: String?) = apply {
        this.accessTokenType = accessTokenType
    }

    fun accessTokenScopes(accessTokenScopes: String?) = apply {
        this.accessTokenScopes = accessTokenScopes
    }

    fun refreshTokenValue(refreshTokenValue: String?) = apply {
        this.refreshTokenValue = refreshTokenValue
    }

    fun refreshTokenIssuedAt(refreshTokenIssuedAt: Instant?) = apply {
        this.refreshTokenIssuedAt = refreshTokenIssuedAt
    }

    fun refreshTokenExpiresAt(refreshTokenExpiresAt: Instant?) = apply {
        this.refreshTokenExpiresAt = refreshTokenExpiresAt
    }

    fun refreshTokenMetadata(refreshTokenMetadata: String?) = apply {
        this.refreshTokenMetadata = refreshTokenMetadata
    }

    fun oidcIdTokenValue(oidcIdTokenValue: String?) = apply {
        this.oidcIdTokenValue = oidcIdTokenValue
    }

    fun oidcIdTokenIssuedAt(oidcIdTokenIssuedAt: Instant?) = apply {
        this.oidcIdTokenIssuedAt = oidcIdTokenIssuedAt
    }

    fun oidcIdTokenExpiresAt(oidcIdTokenExpiresAt: Instant?) = apply {
        this.oidcIdTokenExpiresAt = oidcIdTokenExpiresAt
    }

    fun oidcIdTokenMetadata(oidcIdTokenMetadata: String?) = apply {
        this.oidcIdTokenMetadata = oidcIdTokenMetadata
    }

    fun oidcIdTokenClaims(oidcIdTokenClaims: String?) = apply {
        this.oidcIdTokenClaims = oidcIdTokenClaims
    }

    fun userCodeValue(userCodeValue: String?) = apply {
        this.userCodeValue = userCodeValue
    }

    fun userCodeIssuedAt(userCodeIssuedAt: Instant?) = apply {
        this.userCodeIssuedAt = userCodeIssuedAt
    }

    fun userCodeExpiresAt(userCodeExpiresAt: Instant?) = apply {
        this.userCodeExpiresAt = userCodeExpiresAt
    }

    fun userCodeMetadata(userCodeMetadata: String?) = apply {
        this.userCodeMetadata = userCodeMetadata
    }

    fun deviceCodeValue(deviceCodeValue: String?) = apply {
        this.deviceCodeValue = deviceCodeValue
    }

    fun deviceCodeIssuedAt(deviceCodeIssuedAt: Instant?) = apply {
        this.deviceCodeIssuedAt = deviceCodeIssuedAt
    }

    fun deviceCodeExpiresAt(deviceCodeExpiresAt: Instant?) = apply {
        this.deviceCodeExpiresAt = deviceCodeExpiresAt
    }

    fun deviceCodeMetadata(deviceCodeMetadata: String?) = apply {
        this.deviceCodeMetadata = deviceCodeMetadata
    }

    fun build(): Authorization {
        return Authorization(
            id = id,
            registeredClientId = registeredClientId,
            principalName = principalName,
            authorizationGrantType = authorizationGrantType,
            authorizedScopes = authorizedScopes,
            attributes = attributes,
            state = state,
            authorizationCodeValue = authorizationCodeValue,
            authorizationCodeIssuedAt = authorizationCodeIssuedAt,
            authorizationCodeExpiresAt = authorizationCodeExpiresAt,
            authorizationCodeMetadata = authorizationCodeMetadata,
            accessTokenValue = accessTokenValue,
            accessTokenIssuedAt = accessTokenIssuedAt,
            accessTokenExpiresAt = accessTokenExpiresAt,
            accessTokenMetadata = accessTokenMetadata,
            accessTokenType = accessTokenType,
            accessTokenScopes = accessTokenScopes,
            refreshTokenValue = refreshTokenValue,
            refreshTokenIssuedAt = refreshTokenIssuedAt,
            refreshTokenExpiresAt = refreshTokenExpiresAt,
            refreshTokenMetadata = refreshTokenMetadata,
            oidcIdTokenValue = oidcIdTokenValue,
            oidcIdTokenIssuedAt = oidcIdTokenIssuedAt,
            oidcIdTokenExpiresAt = oidcIdTokenExpiresAt,
            oidcIdTokenMetadata = oidcIdTokenMetadata,
            oidcIdTokenClaims = oidcIdTokenClaims,
            userCodeValue = userCodeValue,
            userCodeIssuedAt = userCodeIssuedAt,
            userCodeExpiresAt = userCodeExpiresAt,
            userCodeMetadata = userCodeMetadata,
            deviceCodeValue = deviceCodeValue,
            deviceCodeIssuedAt = deviceCodeIssuedAt,
            deviceCodeExpiresAt = deviceCodeExpiresAt,
            deviceCodeMetadata = deviceCodeMetadata
        )
    }
}