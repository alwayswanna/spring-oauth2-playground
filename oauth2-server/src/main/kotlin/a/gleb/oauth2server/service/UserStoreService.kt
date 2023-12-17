package a.gleb.oauth2server.service

import a.gleb.oauth2server.db.repository.AccountRepository
import a.gleb.oauth2server.exception.UserNotFoundException
import a.gleb.oauth2server.logger
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class UserStoreService(
    private val accountRepository: AccountRepository
) : Consumer<OAuth2User> {

    override fun accept(t: OAuth2User) {
        logger.info { "Accept social login: ${t.authorities}" }
        val account = accountRepository.findAccountByPreferredUsername(t.name)
            ?: throw UserNotFoundException("User does not exits in data base. [username=${t.name}]")

        logger.info { "User login: [id=${account.id}]" }
    }
}