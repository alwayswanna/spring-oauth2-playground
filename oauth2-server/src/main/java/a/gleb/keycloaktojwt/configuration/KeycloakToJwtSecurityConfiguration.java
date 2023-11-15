package a.gleb.keycloaktojwt.configuration;

import a.gleb.keycloaktojwt.configuration.properties.KeycloakToJwtConfigurationProperties;
import a.gleb.keycloaktojwt.db.entity.AccountRole;
import a.gleb.keycloaktojwt.db.repository.KeycloakAuthorizationSessionRepository;
import a.gleb.keycloaktojwt.service.*;
import a.gleb.keycloaktojwt.utils.OAuth2RegisteredClientUtils;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableConfigurationProperties(value = KeycloakToJwtConfigurationProperties.class)
public class KeycloakToJwtSecurityConfiguration {

    private static final String ROLE_CLAIM = "role";
    private static final String USER_ID_CLAIM = "user_id";

    private final KeycloakToJwtConfigurationProperties properties;

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http
    ) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());
        http
                .cors(it -> {
                    var urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
                    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", properties.getCors());
                    it.configurationSource(urlBasedCorsConfigurationSource);
                });
        http
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login")
                        )
                )
                .oauth2ResourceServer(it -> it.jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(
            JdbcTemplate jdbcTemplate,
            PasswordEncoder passwordEncoder,
            OAuth2AuthorizationServerProperties oAuth2AuthorizationServerProperties
    ) {
        JdbcRegisteredClientRepository jdbcRegisteredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

        oAuth2AuthorizationServerProperties.getClient()
                .entrySet()
                .stream()
                .filter(it -> jdbcRegisteredClientRepository.findByClientId(it.getKey()) == null)
                .map(it -> OAuth2RegisteredClientUtils.build(it, passwordEncoder))
                .forEach(it -> {
                    log.info("Save new registered client with, [client_id={}]", it.getClientId());
                    jdbcRegisteredClientRepository.save(it);
                });

        return jdbcRegisteredClientRepository;
    }

    @Bean
    public AbstractUserDetailsAuthenticationProvider abstractUserDetailsAuthenticationProvider(
            AuthServerUserDetailsService authServerUserDetailsService,
            KeycloakAuthorizationService keycloakAuthorizationService,
            KeycloakAuthorizationSessionRepository keycloakAuthorizationSessionRepository
    ) {
        return new KeycloakAuthenticationProvider(
                authServerUserDetailsService,
                keycloakAuthorizationService,
                keycloakAuthorizationSessionRepository
        );
    }

    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository
    ) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    OAuth2TokenGenerator<?> tokenGenerator(
            JWKSource<SecurityContext> jwkSource,
            AccountService accountService
    ) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(context -> {
            var account = accountService.findAccountByUsername(context.getPrincipal().getName());
            var roles = account.getRoles().stream().map(AccountRole::getRoleName).collect(Collectors.toSet());
            context.getClaims().claim(ROLE_CLAIM, roles);
            context.getClaims().claim(USER_ID_CLAIM, account.getId());
        });

        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator,
                new OAuth2AccessTokenGenerator(),
                new OAuth2RefreshTokenGenerator()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(13);
    }

    @Bean
    public AuthorizedClientServiceOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService
    ) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                oAuth2AuthorizedClientService
        );
    }
}

