package a.gleb.oauth2server.service.authorization

import a.gleb.oauth2server.db.entity.authorization.Authorization
import a.gleb.oauth2server.db.repository.authorization.AuthorizationRepository
import a.gleb.oauth2server.exception.InvalidOAuth2OperationException
import a.gleb.oauth2server.mapper.authorization.AuthorizationMapper
import org.springframework.dao.DataRetrievalFailureException
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.*
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component

@Component
class AuthorizationService(
    private val authorizationRepository: AuthorizationRepository,
    private val oAuth2ClientRepository: RegisteredClientRepository,
    private val authorizationMapper: AuthorizationMapper
) : OAuth2AuthorizationService {

    override fun save(authorization: OAuth2Authorization?) {
        if (authorization == null) {
            throw InvalidOAuth2OperationException("Attempt to save null as authorization")
        }

        authorizationRepository.save(authorizationMapper.toOAuth2Authorization(authorization))
    }

    override fun remove(authorization: OAuth2Authorization?) {
        if (authorization == null) {
            throw InvalidOAuth2OperationException("Attempt to remove nullable authorization")
        }

        authorizationRepository.deleteById(authorization.id)
    }

    override fun findById(id: String?): OAuth2Authorization? {
        if (id.isNullOrEmpty()) {
            throw InvalidOAuth2OperationException("Attempt to search authorization with nullable id")
        }

        val authorizationOptional = authorizationRepository.findById(id)

        if (authorizationOptional.isPresent) {
            val authorization = authorizationOptional.get()
            return authorizationMapper.toOAuth2Authorization(authorization, getAuthorizationClient(authorization))
        } else {
            return null
        }
    }

    override fun findByToken(token: String?, tokenType: OAuth2TokenType?): OAuth2Authorization? {
        if (token.isNullOrEmpty()) {
            throw InvalidOAuth2OperationException("Attempt to search authorization with nullable token")
        }

        val authorization = when (tokenType?.value) {
            null -> authorizationRepository.findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
                token
            )

            STATE -> authorizationRepository.findByState(token)
            ACCESS_TOKEN -> authorizationRepository.findByAccessTokenValue(token)
            CODE -> authorizationRepository.findByAuthorizationCodeValue(token)
            REFRESH_TOKEN -> authorizationRepository.findByRefreshTokenValue(token)
            OidcParameterNames.ID_TOKEN -> authorizationRepository.findByOidcIdTokenValue(token)
            USER_CODE -> authorizationRepository.findByUserCodeValue(token)
            DEVICE_CODE -> authorizationRepository.findByDeviceCodeValue(token)
            else -> { null }
        }

        return if (authorization != null) {
            authorizationMapper.toOAuth2Authorization(authorization, getAuthorizationClient(authorization))
        } else {
            null
        }
    }

    private fun getAuthorizationClient(authorization: Authorization): RegisteredClient {
        val client = oAuth2ClientRepository.findById(authorization.registeredClientId)
            ?: throw DataRetrievalFailureException("The client with id ${authorization.registeredClientId} was not found")

        return client
    }
}