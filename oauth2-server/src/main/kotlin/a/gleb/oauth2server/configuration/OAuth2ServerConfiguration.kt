package a.gleb.oauth2server.configuration

import a.gleb.oauth2server.configuration.properties.OAuth2ServerConfigurationProperties
import a.gleb.oauth2server.configuration.security.KeycloakAuthenticationProvider
import a.gleb.oauth2server.db.repository.KeycloakAuthorizationSessionRepository
import a.gleb.oauth2server.logger
import a.gleb.oauth2server.service.AccountService
import a.gleb.oauth2server.service.KeycloakAuthorizationService
import a.gleb.oauth2server.utils.build
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

const val USER_ID_CLAIM = "user_id"
const val ROLE_CLAIM = "role"
const val PASSWORD_ENCODER_STRENGTH = 13

@Configuration
@EnableConfigurationProperties(OAuth2ServerConfigurationProperties::class)
class OAuth2ServerConfiguration(
    private val properties: OAuth2ServerConfigurationProperties
) {

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder().build()
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity)

        httpSecurity
            .getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())

        return httpSecurity
            .cors {
                val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
                urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", properties.corsConfiguration)
                it.configurationSource(urlBasedCorsConfigurationSource)
            }
            .exceptionHandling {
                it.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login"))
            }
            .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
            .build()
    }

    @Bean
    fun registerClientRepository(
        jdbcTemplate: JdbcTemplate,
        passwordEncoder: PasswordEncoder,
        oAuth2AuthorizationServerProperties: OAuth2AuthorizationServerProperties
    ): RegisteredClientRepository {
        val jdbcRegisteredClientRepository = JdbcRegisteredClientRepository(jdbcTemplate)

        oAuth2AuthorizationServerProperties.client
            .entries
            .asSequence()
            .filter { jdbcRegisteredClientRepository.findByClientId(it.key) == null }
            .map { build(it, passwordEncoder) }
            .forEach {
                logger.info { "Save new client. [client_id=${it.clientId}]" }
                jdbcRegisteredClientRepository.save(it)
            }

        return jdbcRegisteredClientRepository
    }

    @Bean
    fun abstractUserDetailsAuthenticationProvider(
        userDetailsService: UserDetailsService,
        keycloakAuthorizationService: KeycloakAuthorizationService,
        keycloakAuthorizationSessionRepository: KeycloakAuthorizationSessionRepository
    ): AbstractUserDetailsAuthenticationProvider {
        return KeycloakAuthenticationProvider(
            userDetailsService,
            keycloakAuthorizationService,
            keycloakAuthorizationSessionRepository
        )
    }

    @Bean
    fun oAuth2AuthorizationService(
        jdbcTemplate: JdbcTemplate,
        registeredClientRepository: RegisteredClientRepository
    ): OAuth2AuthorizationService {
        return JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository)
    }

    @Bean
    fun jwtEncodingContextOAuth2TokenCustomizer(accountService: AccountService):
            OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(it.tokenType) &&
                it.getPrincipal<Authentication>() is UsernamePasswordAuthenticationToken
            ) {
                val principal = it.getPrincipal<UsernamePasswordAuthenticationToken>()
                val account = accountService.findAccountByUsername(principal.name)
                it.claims.claim(USER_ID_CLAIM, account.id)
                it.claims.claim(ROLE_CLAIM, account.roles.map { role -> role.roleName })
            }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(PASSWORD_ENCODER_STRENGTH)
    }

    @Bean
    fun oAuth2AuthorizeClientManager(
        clientRegistrationRepository: ClientRegistrationRepository,
        oAuth2AuthorizedClientService: OAuth2AuthorizedClientService
    ): AuthorizedClientServiceOAuth2AuthorizedClientManager {
        return AuthorizedClientServiceOAuth2AuthorizedClientManager(
            clientRegistrationRepository,
            oAuth2AuthorizedClientService
        )
    }
}