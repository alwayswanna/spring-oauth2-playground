package a.gleb.cardservice.configuration

import a.gleb.cardservice.configuration.properties.CardServiceConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

private val UNPROTECTED_PATTERNS = listOf(
    "/actuator/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/v3/api-docs/**"
)

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
            .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
            .authorizeHttpRequests {
                it.requestMatchers(*UNPROTECTED_PATTERNS.toTypedArray()).permitAll()
                it.anyRequest().authenticated()
            }
            .build()

    }
}