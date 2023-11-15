package a.gleb.keycloaktojwt.service;

import a.gleb.keycloaktojwt.db.entity.KeycloakAuthorizationSession;
import a.gleb.keycloaktojwt.db.repository.KeycloakAuthorizationSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@Slf4j
public class KeycloakAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final KeycloakAuthorizationService keycloakAuthorizationService;
    private final KeycloakAuthorizationSessionRepository keycloakAuthorizationSessionRepository;

    public KeycloakAuthenticationProvider(
            UserDetailsService userDetailsService,
            KeycloakAuthorizationService keycloakAuthorizationService,
            KeycloakAuthorizationSessionRepository keycloakAuthorizationSessionRepository
    ) {
        this.userDetailsService = userDetailsService;
        this.keycloakAuthorizationService = keycloakAuthorizationService;
        this.keycloakAuthorizationSessionRepository = keycloakAuthorizationSessionRepository;
    }

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication
    ) throws AuthenticationException {
        log.trace(
                "Start authorize on keycloak server with: [username={}, isEnabled={}, timestamp={}]",
                userDetails.getUsername(),
                userDetails.isEnabled(),
                LocalDateTime.now()
        );

        OAuth2AccessToken authorize;
        try {
            authorize = keycloakAuthorizationService.authorize(
                    userDetails.getUsername(),
                    authentication.getCredentials().toString()
            );
        } catch (Exception e) {
            log.error(
                    "Error while authorize on keycloak server with: [username={}, isEnabled={}, timestamp={}]",
                    userDetails.getUsername(),
                    userDetails.isEnabled(),
                    LocalDateTime.now(),
                    e
            );
            throw new BadCredentialsException(
                    this.messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"
                    )
            );
        }

        validateAuthorizeToken(authorize);

        String scopes = String.join(",", authorize.getScopes());

        KeycloakAuthorizationSession session = KeycloakAuthorizationSession.builder()
                .username(userDetails.getUsername())
                .scopes(scopes)
                .accessTokenExpireIn(
                        LocalDateTime.ofInstant(
                                Objects.requireNonNull(authorize.getExpiresAt()), ZoneId.systemDefault()
                        )
                )
                .sessionState("_session_state_")
                .createAt(LocalDateTime.now())
                .build();

        keycloakAuthorizationSessionRepository.save(session);

        log.trace(
                "Finish authorize on keycloak server with: [username={}, isEnabled={}, timestamp={}]",
                userDetails.getUsername(),
                userDetails.isEnabled(),
                LocalDateTime.now()
        );
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return userDetailsService.loadUserByUsername(username);
    }

    private void validateAuthorizeToken(OAuth2AccessToken authorize) {
        if (authorize == null || authorize.getTokenValue() == null) {
            throw new BadCredentialsException(
                    this.messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials",
                            "Bad credentials"
                    )
            );
        }
    }
}
