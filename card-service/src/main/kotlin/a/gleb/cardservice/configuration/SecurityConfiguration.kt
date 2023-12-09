package a.gleb.cardservice.configuration

import a.gleb.cardservice.configuration.properties.CardServiceConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

private val UNPROTECTED_PATTERNS = listOf(
    "/actuator/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/v3/api-docs/**"
)
const val ROLE_CLAIM = "role"
const val SCOPE_CLAIM = "scope"
const val CREDENTIAL_TOKEN = "oauth2-server-token"

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CardServiceConfigurationProperties::class)
class SecurityConfiguration(
    private val properties: CardServiceConfigurationProperties
) {

    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .cors {
                val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
                urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", properties.corsConfiguration)
                it.configurationSource(urlBasedCorsConfigurationSource)
            }
            .oauth2ResourceServer {
                it.jwt { jwtConfigurer ->
                    jwtConfigurer.jwtAuthenticationConverter { converter -> jwtAuthenticationConverter(converter) }
                }
            }
            .authorizeHttpRequests { customizeRequestMatcher(it) }
            .build()

    }

    private fun customizeRequestMatcher(
        matcher: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
    ) {
        matcher.requestMatchers(*UNPROTECTED_PATTERNS.toTypedArray()).permitAll()

        properties.securityConstraints.forEach { securityConstraint ->

            val roles = securityConstraint.authRoles
            securityConstraint.securityCollections.forEach { securityCollection ->
                val patterns = securityCollection.patterns

                if (securityCollection.methods == null || securityCollection.methods!!.isEmpty()) {
                    matcher.requestMatchers(*patterns.toTypedArray()).hasAnyRole(*roles.toTypedArray())
                } else {
                    securityCollection.methods!!.forEach { method ->
                        matcher.requestMatchers(HttpMethod.valueOf(method), *patterns.toTypedArray())
                            .hasAnyRole(*roles.toTypedArray())
                    }
                }

            }
        }

        matcher.anyRequest().authenticated()
    }

    private fun jwtAuthenticationConverter(converter: Jwt): AbstractAuthenticationToken {
        val grantedAuthorities: List<GrantedAuthority> = if (isRoleExists(converter)) {
            (converter.claims[ROLE_CLAIM] as List<*>)
                .asSequence()
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

    private fun isRoleExists(converter: Jwt) =
        converter.claims[ROLE_CLAIM] != null && (converter.claims[ROLE_CLAIM] as List<*>).isNotEmpty()

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