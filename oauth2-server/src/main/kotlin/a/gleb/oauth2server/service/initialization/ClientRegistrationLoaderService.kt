package a.gleb.oauth2server.service.initialization

import a.gleb.oauth2server.logger
import a.gleb.oauth2server.utils.build
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Service

@Service
class ClientRegistrationLoaderService(
    private val passwordEncoder: PasswordEncoder,
    private val registeredClientRepository: RegisteredClientRepository,
    private val oAuth2AuthorizationServerProperties: OAuth2AuthorizationServerProperties
) : CommandLineRunner {

    /**
     * Load and save client from .yaml config when application starts.
     */
    override fun run(vararg args: String?) {
        logger.info { "Load clients from .yaml config." }
        try {
            oAuth2AuthorizationServerProperties.client.entries
                .filter { registeredClientRepository.findByClientId(it.key) == null }
                .map { build(it, passwordEncoder) }.forEach {
                    logger.info { "Create new client. [clientId=${it.clientId}]" }
                    registeredClientRepository.save(it)
                }
            logger.info { "Clients from .yaml config was loaded" }
        } catch (e: Exception) {
            logger.error { "Error while load clients from .yaml config. [message=${e.message}]" }
        }
    }
}