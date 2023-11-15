package a.gleb.cardservice.configuration;

import a.gleb.cardservice.configuration.properties.CardServiceConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CardServiceConfigurationProperties.class)
public class SecurityConfiguration {

    private final CardServiceConfigurationProperties properties;

    private static final List<String> UNPROTECTED_PATTERNS = List.of(
            "/actuator/**",
            "/api/v3/docs",
            "/swagger-ui.html",
            "/swagger-ui/**"
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(UNPROTECTED_PATTERNS.toArray(String[]::new)).permitAll();
                    auth.requestMatchers("/api/v1/card").authenticated();
                })
                .cors(it -> {
                    var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
                    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", properties.getCorsConfiguration());
                    it.configurationSource(urlBasedCorsConfigurationSource);
                })
                .oauth2ResourceServer(it -> it.jwt(Customizer.withDefaults()))
                .build();
    }
}
