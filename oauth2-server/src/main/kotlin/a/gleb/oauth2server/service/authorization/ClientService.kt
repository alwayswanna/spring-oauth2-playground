package a.gleb.oauth2server.service.authorization

import a.gleb.oauth2server.db.repository.authorization.ClientRepository
import a.gleb.oauth2server.exception.InvalidOAuth2OperationException
import a.gleb.oauth2server.mapper.authorization.ClientMapper
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component


@Component
class ClientService(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper
) : RegisteredClientRepository {

    override fun save(registeredClient: RegisteredClient?) {
        if (registeredClient == null) {
            throw InvalidOAuth2OperationException("Attempt to save null as registered client.")
        }

        clientRepository.save(clientMapper.toEntity(registeredClient))
    }

    override fun findById(id: String?): RegisteredClient? {
        if (id == null) {
            throw InvalidOAuth2OperationException("Attempt search registered client with nullable id.")
        }

        return clientMapper.toRegisteredClient(clientRepository.findById(id).orElse(null))
    }

    override fun findByClientId(clientId: String?): RegisteredClient? {
        if (clientId == null) {
            throw InvalidOAuth2OperationException("Attempt search registered client with nullable clientId.")
        }

        return clientMapper.toRegisteredClient(clientRepository.findByClientId(clientId))
    }
}