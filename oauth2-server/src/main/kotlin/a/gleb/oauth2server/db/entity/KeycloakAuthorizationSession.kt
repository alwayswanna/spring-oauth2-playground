package a.gleb.oauth2server.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.AUTO
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity(name = "keycloak_authorization_session")
data class KeycloakAuthorizationSession(

    @Id
    @GeneratedValue(strategy = AUTO)
    var id: UUID?,

    @Column(name = "session_state")
    var sessionState: String,

    @Column(name = "scopes")
    var scopes: String,

    @Column(name = "access_token_expire_in")
    var accessTokenExpireIn: LocalDateTime,

    @Column(name = "username")
    var username: String,

    @Column(name = "create_at")
    var createdAt: LocalDateTime
)
