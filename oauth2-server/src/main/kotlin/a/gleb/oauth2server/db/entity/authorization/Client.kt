package a.gleb.oauth2server.db.entity.authorization

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.Instant

@Entity
@Table(name = "oauth2_registered_client")
data class Client(

    @Id
    @Column(name = "id")
    var id: String,

    @Column(name = "client_id")
    var clientId: String,

    @Column(name = "client_id_issued_at")
    var clientIdIssuedAt: Instant,

    @Column(name = "client_secret")
    var clientSecret: String?,

    @Column(name = "client_secret_expires_at")
    var clientSecretExpiresAt: Instant?,

    @Column(name = "client_name")
    var clientName: String,

    @Column(name = "client_authentication_methods", length = 1000)
    var clientAuthenticationMethods: String,

    @Column(name = "authorization_grant_types", length = 1000)
    var authorizationGrantTypes: String,

    @Column(name = "redirect_uris", length = 1000)
    var redirectUris: String?,

    @Column(name = "post_logout_redirect_uris", length = 1000)
    var postLogoutRedirectUris: String?,

    @Column(name = "scopes", length = 1000)
    var scopes: String,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "client_settings", length = 2000, columnDefinition = "jsonb")
    var clientSettings: MutableMap<String, Any?> = mutableMapOf(),

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "token_settings", length = 2000, columnDefinition = "jsonb")
    var tokenSettings: MutableMap<String, Any?> = mutableMapOf()
)