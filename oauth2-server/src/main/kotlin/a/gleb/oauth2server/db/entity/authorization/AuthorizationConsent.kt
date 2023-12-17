package a.gleb.oauth2server.db.entity.authorization

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

@Entity
@Table(name = "oauth2_authorization_consent")
@IdClass(AuthorizationConsentId::class)
data class AuthorizationConsent(
    @Id
    @Column(name = "registered_client_id")
    var registeredClientId: String?,
    @Id
    @Column(name = "principal_name")
    var principalName: String?,
    @Column(name = "authorities", length = 1000)
    var authorities: String?
)

data class AuthorizationConsentId(
    var registeredClientId: String?,
    var principalName: String?,
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as AuthorizationConsentId
        return registeredClientId == that.registeredClientId && principalName == that.principalName
    }

    override fun hashCode(): Int {
        return Objects.hash(registeredClientId, principalName)
    }
}