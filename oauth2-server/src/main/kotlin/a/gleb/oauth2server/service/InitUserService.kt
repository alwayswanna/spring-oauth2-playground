package a.gleb.oauth2server.service

import a.gleb.oauth2server.configuration.properties.OAuth2ServerConfigurationProperties
import a.gleb.oauth2server.db.entity.Account
import a.gleb.oauth2server.db.entity.AccountRole
import a.gleb.oauth2server.db.repository.AccountRepository
import a.gleb.oauth2server.db.repository.AccountRoleRepository
import a.gleb.oauth2server.logger
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils
import java.nio.file.Files
import java.nio.file.Path

const val ACCOUNTS_FILENAME = "classpath:init/accounts.json"
const val ROLES_FILENAME = "classpath:init/roles.json"

@Component
class InitUserService(
    private val objectMapper: ObjectMapper,
    private val passwordEncoder: PasswordEncoder,
    private val accountRepository: AccountRepository,
    private val accountRoleRepository: AccountRoleRepository,
    private val properties: OAuth2ServerConfigurationProperties
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        logger.info { "Init database: create roles & accounts." }

        if (properties.loadRoles) {
            logger.info { "Init database: load roles" }
            proceedRoles()
        }

        if (properties.loadUsers) {
            logger.info { "Init database: load account" }
            proceedAccounts()
        }

        logger.info { "Init database: all entities are loaded" }
    }

    private fun proceedAccounts() {
        val file = ResourceUtils.getFile(ACCOUNTS_FILENAME)
        val accountsJson = Files.readString(Path.of(file.path))
        val accounts = objectMapper.readValue<List<Account>>(accountsJson)

        val roles = accountRoleRepository.findAll().toSet()
        accounts.asSequence()
            .filter { accountRepository.findAccountByUsername(it.username) == null }
            .forEach {
                saveAccount(it, roles)
            }

    }

    private fun saveAccount(it: Account, roles: Set<AccountRole>) {
        it.password = passwordEncoder.encode(it.password)
        it.disabled = false
        it.roles = roles

        accountRepository.save(it)
    }

    private fun proceedRoles() {
        val file = ResourceUtils.getFile(ROLES_FILENAME)
        val rolesJson = Files.readString(Path.of(file.path))
        val roles = objectMapper.readValue<List<String>>(rolesJson)

        val existingRoles = accountRoleRepository.findAll()
            .asSequence()
            .map { it.roleName }
            .toList()

        val rolesToSave = roles.asSequence()
            .filter { !existingRoles.contains(it) }
            .map { AccountRole(null, it) }
            .toList()

        accountRoleRepository.saveAll(rolesToSave)
    }
}