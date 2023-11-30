package a.gleb.oauth2server.service

import a.gleb.oauth2server.db.repository.AccountRepository
import a.gleb.oauth2server.logger
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class OAuth2UserDetailsService(
    private val accountRepository: AccountRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        logger.info { "Load user with username $username" }
        if (username == null) {
            logger.error { "Nullable username in request" }
            throw UsernameNotFoundException("Request with nullable username param.")
        }

        val account = accountRepository.findAccountByUsername(username)
            ?: throw UsernameNotFoundException("User with username: $username, is not found")

        logger.info { "Map roles to UserDetails: ${account.roles}" }
        val roles = account.roles
            .asSequence()
            .map { SimpleGrantedAuthority(it.roleName) }
            .toList()

        return User.builder()
            .username(account.username)
            .password(account.password)
            .disabled(account.disabled)
            .authorities(roles)
            .build()
    }
}