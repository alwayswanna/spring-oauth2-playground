package a.gleb.oauth2server.service.token

import a.gleb.oauth2server.db.entity.Account
import a.gleb.oauth2server.db.repository.AccountRepository
import a.gleb.oauth2server.db.repository.RoleRepository
import a.gleb.oauth2server.exception.RoleNotFoundException
import a.gleb.oauth2server.logger
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer

const val SUB_CLAIM: String = "sub"
const val ROLES_CLAIM: String = "role"
const val USER_ID_CLAIM: String = "user_id"
const val DEFAULT_ROLE: String = "DEFAULT"

class OidcAndAccessTokenResponseCustomizerService(
    private val roleRepository: RoleRepository,
    private val accountRepository: AccountRepository
) : OAuth2TokenCustomizer<JwtEncodingContext> {

    /**
     * Customize ID_TOKEN && ACCESS_TOKEN, add 'roles' as claims.
     */
    override fun customize(context: JwtEncodingContext?) {
        logger.info { "Customize token claims" }
        context?.claims?.claims {
            val username = it[SUB_CLAIM]
            if (username != null) {
                it.putAll(innerCustomizeClaims(username as String))
            }
        }

        logger.info { "End customize token claims" }
    }

    /**
     * Method create custom claim for token.
     */
    private fun innerCustomizeClaims(username: String): Map<String, Any> {
        val account: Account? = accountRepository.findAccountByPreferredUsername(username)
        if (account != null) {
            val setRoles = account.roles.asSequence().map { it.roleName }.toSet()
            return mapOf(ROLES_CLAIM to setRoles, USER_ID_CLAIM to account.id!!)
        } else {
            val defaultRole = roleRepository.findAccountRoleByRoleName(DEFAULT_ROLE)
                ?: throw RoleNotFoundException("Role with name $DEFAULT_ROLE is not found for client_credentials flow")
            return mapOf(ROLES_CLAIM to listOf(defaultRole.roleName))
        }
    }
}