package a.gleb.oauth2server.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.AUTO
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "oauth2_authorization")
data class OAuth2AuthorizationInner(

    @Id
    @GeneratedValue(strategy = AUTO)
    var id: String?,

    @Column(name = "principal_name")
    var principalName: String,

    @Column(name = "access_token_issued_at")
    var accessTokenIssuedAt: Instant
)
