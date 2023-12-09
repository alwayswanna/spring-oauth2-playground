package a.gleb.oauth2server.service

import a.gleb.oauth2server.db.entity.Account
import a.gleb.oauth2server.db.repository.AccountRepository
import a.gleb.oauth2server.logger
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {

    fun findAccountByUsername(username: String): Account {
        logger.info { "Search user with username. [username=$username]" }
        return accountRepository.findAccountByUsername(username)
            ?: throw UsernameNotFoundException("User with username=$username does not found")
    }
}