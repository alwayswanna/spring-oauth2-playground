package a.gleb.oauth2server.configuration

import a.gleb.oauth2server.configuration.properties.OAuth2ServerConfigurationProperties
import a.gleb.oauth2server.db.repository.AccountRepository
import a.gleb.oauth2server.db.repository.RoleRepository
import a.gleb.oauth2server.jose.generateRsa
import a.gleb.oauth2server.service.token.OidcAndAccessTokenResponseCustomizerService
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration(proxyBeanMethods = false)
class OAuth2ServerConfiguration(
    private val properties: OAuth2ServerConfigurationProperties
) {

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity)

        httpSecurity
            .getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())

        httpSecurity
            .cors {
                val corsConfiguration = UrlBasedCorsConfigurationSource()
                corsConfiguration.registerCorsConfiguration("/**", properties.corsConfiguration)
                it.configurationSource(corsConfiguration)
            }
            .csrf { it.disable() }
            .exceptionHandling {
                it.defaultAuthenticationEntryPointFor(
                    LoginUrlAuthenticationEntryPoint("/login"),
                    MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                )
            }
            .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }

        return httpSecurity.build()
    }

    @Bean
    fun oAuth2TokenCustomizer(
        roleRepository: RoleRepository,
        accountRepository: AccountRepository
    ): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OidcAndAccessTokenResponseCustomizerService(roleRepository, accountRepository)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder(properties.passwordEncoderStrength)
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey = generateRsa()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector, _ -> jwkSelector.select(jwkSet) }
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext?>?): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder().build()
    }
}