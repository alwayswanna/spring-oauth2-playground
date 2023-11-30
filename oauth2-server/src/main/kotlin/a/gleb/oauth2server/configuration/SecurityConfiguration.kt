package a.gleb.oauth2server.configuration

import a.gleb.oauth2server.configuration.properties.OAuth2ServerConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfiguration(
    private val properties: OAuth2ServerConfigurationProperties
) {

    @Bean
    fun securityFilterChainInner(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .cors {
                val urlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
                urlBasedCorsConfigurationSource.registerCorsConfiguration(
                    "/**",
                    properties.corsConfiguration
                )
                it.configurationSource(urlBasedCorsConfigurationSource)
            }
            .authorizeHttpRequests {
                it.requestMatchers("/actuator/**").permitAll()
                it.anyRequest().authenticated()
            }
            .formLogin { Customizer.withDefaults<Any>() }
            .build()
    }
}