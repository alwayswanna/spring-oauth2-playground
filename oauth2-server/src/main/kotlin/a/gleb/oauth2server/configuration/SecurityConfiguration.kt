package a.gleb.oauth2server.configuration

import a.gleb.oauth2server.configuration.properties.OAuth2ServerConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfiguration {

    @Bean
    fun defaultSecurityFilterChain(
        httpSecurity: HttpSecurity,
        properties: OAuth2ServerConfigurationProperties
    ): SecurityFilterChain {
        return httpSecurity
            .cors {
                val urlBasedCorsConfiguration = UrlBasedCorsConfigurationSource()
                urlBasedCorsConfiguration.registerCorsConfiguration("/**", properties.corsConfiguration)
                it.configurationSource(urlBasedCorsConfiguration)
            }
            .oauth2ResourceServer { it.jwt(Customizer.withDefaults()) }
            .authorizeHttpRequests {
                it.requestMatchers("/actuator/**").permitAll()
                it.anyRequest().authenticated()
            }
            .formLogin(Customizer.withDefaults())
            .build()
    }
}