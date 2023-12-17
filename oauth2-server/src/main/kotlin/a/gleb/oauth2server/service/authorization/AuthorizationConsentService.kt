package a.gleb.oauth2server.service.authorization

import a.gleb.oauth2server.db.repository.authorization.AuthorizationConsentRepository
import a.gleb.oauth2server.exception.InvalidOAuth2OperationException
import a.gleb.oauth2server.mapper.authorization.AuthorizationConsentMapper
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component

@Component
class AuthorizationConsentService(
    private val authorizationConsentRepository: AuthorizationConsentRepository,
    private val registeredClientRepository: RegisteredClientRepository,
    private val authorizationConsentMapper: AuthorizationConsentMapper
) : OAuth2AuthorizationConsentService {

    override fun save(authorizationConsent: OAuth2AuthorizationConsent?) {
        if (authorizationConsent == null) {
            throw InvalidOAuth2OperationException("Attempt to save nullable consent entity")
        }

        authorizationConsentRepository.save(authorizationConsentMapper.toAuthorizationConsent(authorizationConsent))
    }

    override fun remove(authorizationConsent: OAuth2AuthorizationConsent?) {
        if (authorizationConsent == null) {
            throw InvalidOAuth2OperationException("Attempt to delete nullable authorization consent")
        }

        authorizationConsentRepository.deleteByRegisteredClientIdAndPrincipalName(
            authorizationConsent.registeredClientId,
            authorizationConsent.principalName
        )
    }

    override fun findById(registeredClientId: String?, principalName: String?): OAuth2AuthorizationConsent? {
        if (registeredClientId.isNullOrEmpty() || principalName.isNullOrEmpty()) {
            throw InvalidOAuth2OperationException("Attempt to search by nullable principalName or nullable registrationClientId. [registrationClientId=$registeredClientId, principalName=$principalName]")
        }

        val consent = authorizationConsentRepository.findByRegisteredClientIdAndPrincipalName(
            registeredClientId,
            principalName
        )

        if (consent == null) {
            return null
        }

        val registeredClient = registeredClientRepository.findById(consent.registeredClientId)
            ?: throw InvalidOAuth2OperationException("The client with id $registeredClientId was not found")

        return authorizationConsentMapper.toOAuth2AuthorizationConsent(consent, registeredClient)
    }
}