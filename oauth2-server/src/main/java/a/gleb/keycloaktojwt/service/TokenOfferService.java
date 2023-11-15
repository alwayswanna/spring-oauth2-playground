package a.gleb.keycloaktojwt.service;

import a.gleb.keycloaktojwt.db.repository.KeycloakAuthorizationSessionRepository;
import a.gleb.keycloaktojwt.db.repository.OAuth2AuthorizationInnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.security.oauth2.server.authorization.OAuth2TokenType.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenOfferService {

    private static final String PREFERRED_USERNAME_CLAIM = "preferred_username";

    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2AuthorizationInnerRepository oAuth2AuthorizationInnerRepository;
    private final KeycloakAuthorizationSessionRepository keycloakAuthorizationSessionRepository;

    public Map<String, String> offer() {
        SecurityContext context = SecurityContextHolder.getContext();
        var authentication = (Jwt) context.getAuthentication().getPrincipal();
        var tokenClaims = authentication.getClaims();


        var username = tokenClaims.get(PREFERRED_USERNAME_CLAIM);
        var sessionByUsername = oAuth2AuthorizationInnerRepository.findByPrincipalName(username.toString());

        var authorization = authorizationService.findById(sessionByUsername.getId());

        if (authorization != null && authorization.getRefreshToken() != null && authorization.getRefreshToken().getToken() != null) {
            return Map.of(OAuth2ParameterNames.REFRESH_TOKEN, authorization.getRefreshToken().getToken().getTokenValue());
        } else {
            throw new UsernameNotFoundException(String.format("Not found saved session for %s", username));
        }
    }
}
