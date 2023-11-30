package a.gleb.oauth2server.configuration

import a.gleb.oauth2server.configuration.properties.OAuth2ServerConfigurationProperties
import a.gleb.oauth2server.utils.build
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableConfigurationProperties(OAuth2ServerConfigurationProperties::class)
class OAuth2ServerConfiguration(
    private val properties: OAuth2ServerConfigurationProperties
) {

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {

        if (properties.customizeUserInfoResponse) {
            val oAuth2AuthorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
            val endpointsMatcher = oAuth2AuthorizationServerConfigurer.endpointsMatcher

            val userInfoMapper: (t: OidcUserInfoAuthenticationContext) -> OidcUserInfo = { context ->
                val authentication = context.getAuthentication<OidcUserInfoAuthenticationToken>()
                val jwtAuthenticationToken = authentication.principal as JwtAuthenticationToken

                OidcUserInfo(jwtAuthenticationToken.token.claims)
            }

            oAuth2AuthorizationServerConfigurer.oidc { oidc ->
                oidc.userInfoEndpoint {
                    it.userInfoMapper(userInfoMapper)
                }
            }

            httpSecurity
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests { it.anyRequest().authenticated() }
                .csrf { it.ignoringRequestMatchers(endpointsMatcher) }
                .exceptionHandling { it.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login")) }
                .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
                .apply(oAuth2AuthorizationServerConfigurer)

        } else {
            OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity)

            httpSecurity
                .getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
                .oidc(Customizer.withDefaults())

            httpSecurity
                .cors {
                    val urlBasedCorsConfiguration = UrlBasedCorsConfigurationSource()
                    urlBasedCorsConfiguration.registerCorsConfiguration("/**", properties.corsConfiguration)
                    it.configurationSource(urlBasedCorsConfiguration)
                }
                .exceptionHandling { it.authenticationEntryPoint(LoginUrlAuthenticationEntryPoint("/login")) }
                .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
        }

        return httpSecurity.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(13)
    }

    @Bean
    fun registerClientRepository(
        jdbcTemplate: JdbcTemplate,
        passwordEncoder: PasswordEncoder,
        serverProperties: OAuth2AuthorizationServerProperties
    ): RegisteredClientRepository {
        val jdbcRegisteredClientRepository = JdbcRegisteredClientRepository(jdbcTemplate)

        serverProperties.client
            .entries
            .asSequence()
            .filter { jdbcRegisteredClientRepository.findByClientId(it.key) == null }
            .map { build(it, passwordEncoder) }
            .forEach {
                jdbcRegisteredClientRepository.save(it)
            }

        return jdbcRegisteredClientRepository
    }

    @Bean
    fun oAuth2AuthorizationService(
        jdbcTemplate: JdbcTemplate,
        registeredClientRepository: RegisteredClientRepository
    ): OAuth2AuthorizationService {
        return JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository)
    }
}