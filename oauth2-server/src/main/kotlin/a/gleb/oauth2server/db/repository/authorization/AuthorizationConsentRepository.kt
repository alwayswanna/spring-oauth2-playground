package a.gleb.oauth2server.db.repository.authorization

import a.gleb.oauth2server.db.entity.authorization.AuthorizationConsent
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorizationConsentRepository : JpaRepository<AuthorizationConsent, String> {

    fun findByRegisteredClientIdAndPrincipalName(
        registeredClientId: String,
        principalName: String
    ): AuthorizationConsent?

    fun deleteByRegisteredClientIdAndPrincipalName(registeredClientId: String, principalName: String)
}