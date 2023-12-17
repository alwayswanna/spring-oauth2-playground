package a.gleb.oauth2server.configuration

import a.gleb.oauth2server.configuration.properties.OAuth2ServerConfigurationProperties
import a.gleb.oauth2server.logger
import a.gleb.oauth2server.service.UserStoreService
import a.gleb.oauth2server.service.authorization.handler.FederationIdentityAuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.session.HttpSessionEventPublisher
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

private val UNPROTECTED_PATTERNS = listOf(
    "/assets/**",
    "/login",
    "/static/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/v3/api-docs/**"
)
const val ROLE_CLAIM = "role"
const val SCOPE_CLAIM = "scope"
const val CREDENTIAL_TOKEN = "oauth2-server-token"
@Configuration(proxyBeanMethods = false)
class SecurityConfiguration(
    private val properties: OAuth2ServerConfigurationProperties
) {

    @Bean
    fun defaultSecurityFilterChain(
        httpSecurity: HttpSecurity,
        userStoreService: UserStoreService
    ): SecurityFilterChain {
        return httpSecurity
            .cors {
                val corsConfiguration = UrlBasedCorsConfigurationSource()
                corsConfiguration.registerCorsConfiguration("/**", properties.corsConfiguration)
                it.configurationSource(corsConfiguration)
            }
            .csrf { it.disable() }
            .authorizeHttpRequests { customizeRequestMatcherRegistry(it) }
            .formLogin { it.loginPage("/login") }
            .oauth2Login {
                it
                    .loginPage("/login")
                    .successHandler(authenticationSuccessHandler(userStoreService))
            }
            .oauth2ResourceServer{
                it.jwt{ jwtConfigurer ->
                    jwtConfigurer.jwtAuthenticationConverter { jwt -> jwtAuthenticationConverter(jwt) }
                }
            }
            .build()
    }

    private fun customizeRequestMatcherRegistry(
        matcherRegistry: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
    ) {
        logger.info { "Start customize auth endpoints" }

        matcherRegistry.requestMatchers(*UNPROTECTED_PATTERNS.toTypedArray()).permitAll()
        properties.securityConstraints.forEach { securityConstraint ->
            val roles = securityConstraint.roles
            securityConstraint.securityCollections.forEach { securityCollection ->
                val patterns = securityCollection.patterns.toTypedArray()
                if (securityCollection.methods == null || securityCollection.methods!!.isEmpty()) {
                    matcherRegistry.requestMatchers(*patterns).hasAnyRole(*roles.toTypedArray())
                    return
                }

                securityCollection.methods!!.forEach { method ->
                    matcherRegistry.requestMatchers(HttpMethod.valueOf(method), *patterns)
                        .hasAnyRole(*roles.toTypedArray())
                }
            }
        }
        matcherRegistry.anyRequest().authenticated()

        logger.info { "End customize auth endpoints" }
    }

    private fun authenticationSuccessHandler(userStoreService: UserStoreService): AuthenticationSuccessHandler {
        return FederationIdentityAuthenticationSuccessHandler(userStoreService)
    }

    @Bean
    fun sessionRegistry(): SessionRegistry {
        return SessionRegistryImpl()
    }

    @Bean
    fun httpSessionEventPublisher(): HttpSessionEventPublisher {
        return HttpSessionEventPublisher()
    }

    private fun jwtAuthenticationConverter(converter: Jwt): AbstractAuthenticationToken {
        val roles = converter.claims[ROLE_CLAIM] as List<*>
        val grantedAuthorities: List<GrantedAuthority> = if (roles.isNotEmpty()) {
            roles.asSequence()
                .map { "ROLE_$it" }
                .map { SimpleGrantedAuthority(it) }
                .toList()
        } else {
            (converter.claims[SCOPE_CLAIM] as List<*>).asSequence()
                .map { "ROLE_$it" }
                .map { SimpleGrantedAuthority(it) }
                .toList()
        }

        return AbstractAuthenticationTokenImpl(grantedAuthorities, converter)
    }

    private class AbstractAuthenticationTokenImpl(
        authorities: List<GrantedAuthority>,
        private val converter: Jwt
    ) : AbstractAuthenticationToken(authorities) {

        override fun getCredentials(): Any {
            return CREDENTIAL_TOKEN
        }

        override fun getPrincipal(): Any {
            return converter
        }

        override fun isAuthenticated(): Boolean {
            return true
        }
    }
}