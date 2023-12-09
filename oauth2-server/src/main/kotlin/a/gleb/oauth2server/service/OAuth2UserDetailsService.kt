package a.gleb.oauth2server.service

import a.gleb.oauth2server.logger
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

const val DEFAULT_ROLE = "uma_authorization"
const val DEFAULT_EMPTY_PASSWORD = "#PASSWORD#"

@Service
class OAuth2UserDetailsService(
    private val accountService: AccountService
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        logger.info { "Search user with username: $username" }

        if (username.isNullOrBlank()) {
            logger.error { "Error while search user by username. Username is empty." }
            throw UsernameNotFoundException("Request with nullable or blank username")
        }

        with(accountService.findAccountByUsername(username)) {
            return User.builder()
                .username(username)
                .password(DEFAULT_EMPTY_PASSWORD)
                .disabled(disabled)
                .authorities(DEFAULT_ROLE)
                .build()
        }
    }
}