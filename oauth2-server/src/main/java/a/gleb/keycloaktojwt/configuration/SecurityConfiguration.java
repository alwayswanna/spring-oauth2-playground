package a.gleb.keycloaktojwt.configuration;

import a.gleb.keycloaktojwt.configuration.properties.KeycloakToJwtConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(
            HttpSecurity http,
            KeycloakToJwtConfigurationProperties properties
    ) throws Exception {
        http.cors(it -> {
            var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
            urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", properties.getCors());
            it.configurationSource(urlBasedCorsConfigurationSource);
        });

        http
                .oauth2ResourceServer(it -> it.jwt(Customizer.withDefaults()))
                .authorizeHttpRequests(authorize -> {
                            authorize.requestMatchers("/actuator/**").permitAll();
                            authorize.anyRequest().authenticated();
                        }
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
