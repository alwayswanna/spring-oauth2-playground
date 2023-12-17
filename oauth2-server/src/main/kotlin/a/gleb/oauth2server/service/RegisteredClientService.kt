package a.gleb.oauth2server.service

import a.gleb.oauth2server.constants.COMMA
import a.gleb.oauth2server.db.repository.authorization.ClientRepository
import a.gleb.oauth2server.exception.BadRequestException
import a.gleb.oauth2server.exception.RegisteredClientNotFoundException
import a.gleb.oauth2server.logger
import a.gleb.oauth2server.mapper.authorization.ClientMapper
import a.gleb.oauth2server.model.RegisteredClientRequest
import a.gleb.oauth2server.model.RegisteredClientResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.stereotype.Service
import java.util.*

@Service
class RegisteredClientService(
    private val clientMapper: ClientMapper,
    private val passwordEncoder: PasswordEncoder,
    private val clientRepository: ClientRepository
) {

    /**
     * Create new registered client.
     */
    fun create(request: RegisteredClientRequest): RegisteredClientResponse {
        logger.info { "Create new registered client with clientId=${request.clientId}" }
        val client = clientMapper.toEntity(
            RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(request.clientId)
                .clientSecret(request.clientSecret)
                .clientAuthenticationMethods {
                    request.clientAuthenticationMethods?.map { authMethod -> ClientAuthenticationMethod(authMethod) }
                        ?.let { methods -> it.addAll(methods) }
                }
                .authorizationGrantTypes {
                    request.clientAuthorizationGrantTypes?.map { grantType -> AuthorizationGrantType(grantType) }
                        ?.let { types -> it.addAll(types) }
                }
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .scopes { request.scopes?.let { scopes -> it.addAll(scopes) } }
                .redirectUris { request.clientRedirectUris?.let { uris -> it.addAll(uris) } }
                .postLogoutRedirectUris { request.clientPostLogoutUris?.let { uris -> it.addAll(uris) } }

                .build()
        )

        return clientMapper.toResponse(clientRepository.save(client))
    }

    /**
     * Delete registered client by id.
     */
    fun delete(id: UUID) {
        logger.info { "Delete registered client with id=$id" }
        clientRepository.deleteById(id.toString())
        logger.info { "Successfully delete registered client with id=$id" }
    }

    /**
     * Get all registered clients.
     */
    fun getRegisteredClient(): List<RegisteredClientResponse> {
        logger.info { "Search all registered clients." }
        return clientRepository.findAll()
            .map(clientMapper::toResponse)
            .toList()
    }

    /**
     * Update registered client.
     */
    fun update(request: RegisteredClientRequest): RegisteredClientResponse {
        logger.info { "Attempt to update registered client with id=${request.id}" }
        if (request.id == null) {
            throw BadRequestException("Can`t change registered client with request.id is NULL")
        }
        val registeredClient = clientRepository.findById(request.id.toString())
            .orElseThrow { RegisteredClientNotFoundException("Registered client with id=${request.id} does not exists") }
        (request.clientId ?: registeredClient.clientId)
            .also { registeredClient.clientId = it }
        (request.clientSecret ?: passwordEncoder.encode(registeredClient.clientSecret))
            .also { registeredClient.clientSecret = it }

        if (!request.clientAuthorizationGrantTypes.isNullOrEmpty()) {
            registeredClient.authorizationGrantTypes = request.clientAuthorizationGrantTypes
                .map { AuthorizationGrantType(it) }
                .joinToString(COMMA)
        }

        if (!request.clientAuthenticationMethods.isNullOrEmpty()) {
            registeredClient.clientAuthenticationMethods = request.clientAuthenticationMethods
                .map { ClientAuthenticationMethod(it) }
                .joinToString(COMMA)
        }

        if (!request.clientRedirectUris.isNullOrEmpty()) {
            registeredClient.redirectUris = request.clientRedirectUris.joinToString(COMMA)
        }

        if (!request.clientPostLogoutUris.isNullOrEmpty()) {
            registeredClient.postLogoutRedirectUris = request.clientPostLogoutUris.joinToString(COMMA)
        }

        if (!request.scopes.isNullOrEmpty()) {
            registeredClient.scopes = request.scopes.joinToString(COMMA)
        }

        return clientMapper.toResponse(clientRepository.save(registeredClient))
    }
}