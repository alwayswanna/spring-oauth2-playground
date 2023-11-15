package a.gleb.keycloaktojwt.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.security.oauth2.client.OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME;
import static org.springframework.security.oauth2.client.OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME;

@Slf4j
@Service
@AllArgsConstructor
public class KeycloakAuthorizationService {

    private static final String REGISTRATION_ID = "keycloak";

    private final AuthorizedClientServiceOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager;

    @PostConstruct
    public void afterPropertiesSets() {
        oAuth2AuthorizedClientManager.setAuthorizedClientProvider(
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .password()
                        .refreshToken()
                        .build()
        );
    }

    public OAuth2AccessToken authorize(@NonNull String username, @NonNull String password) {
        oAuth2AuthorizedClientManager.setContextAttributesMapper(
                request -> Map.of(
                        USERNAME_ATTRIBUTE_NAME, username,
                        PASSWORD_ATTRIBUTE_NAME, password
                )
        );

        OAuth2AuthorizeRequest authRequest = OAuth2AuthorizeRequest.withClientRegistrationId(REGISTRATION_ID)
                .principal(REGISTRATION_ID)
                .build();

        OAuth2AuthorizedClient authorize = oAuth2AuthorizedClientManager.authorize(authRequest);

        if (authorize == null || authorize.getAccessToken() == null) {
            throw new IllegalStateException(
                    String.format(
                            "Error while authorize on authorization server, [attributes=%s]",
                            authRequest.getAttributes()
                    )
            );
        }

        return authorize.getAccessToken();
    }
}
