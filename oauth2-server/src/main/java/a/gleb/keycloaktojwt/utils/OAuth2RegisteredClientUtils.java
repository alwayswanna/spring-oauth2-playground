package a.gleb.keycloaktojwt.utils;

import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.util.Map;

public final class OAuth2RegisteredClientUtils {

    private OAuth2RegisteredClientUtils() {}

    public static RegisteredClient build(Map.Entry<String, OAuth2AuthorizationServerProperties.Client> it, PasswordEncoder passwordEncoder){
        OAuth2AuthorizationServerProperties.Registration registration = it.getValue().getRegistration();
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        RegisteredClient.Builder builder = RegisteredClient.withId(it.getKey());
        map.from(registration::getClientId).to(builder::clientId);
        map.from(registration::getClientSecret).to(secret -> builder.clientSecret(passwordEncoder.encode(secret)));
        map.from(registration::getClientName).to(builder::clientName);
        registration.getClientAuthenticationMethods()
                .forEach((clientAuthenticationMethod) -> map.from(clientAuthenticationMethod)
                        .as(ClientAuthenticationMethod::new)
                        .to(builder::clientAuthenticationMethod));
        registration.getAuthorizationGrantTypes()
                .forEach((authorizationGrantType) -> map.from(authorizationGrantType)
                        .as(AuthorizationGrantType::new)
                        .to(builder::authorizationGrantType));
        registration.getRedirectUris().forEach((redirectUri) -> map.from(redirectUri).to(builder::redirectUri));
        registration.getPostLogoutRedirectUris()
                .forEach((redirectUri) -> map.from(redirectUri).to(builder::postLogoutRedirectUri));
        registration.getScopes().forEach((scope) -> map.from(scope).to(builder::scope));
        builder.clientSettings(getClientSettings(it.getValue(), map));
        builder.tokenSettings(getTokenSettings(it.getValue(), map));
        return builder.build();
    }

    private static ClientSettings getClientSettings(OAuth2AuthorizationServerProperties.Client client, PropertyMapper map) {
        ClientSettings.Builder builder = ClientSettings.builder();
        map.from(client::isRequireProofKey).to(builder::requireProofKey);
        map.from(client::isRequireAuthorizationConsent).to(builder::requireAuthorizationConsent);
        map.from(client::getJwkSetUri).to(builder::jwkSetUrl);
        map.from(client::getTokenEndpointAuthenticationSigningAlgorithm)
                .as(OAuth2RegisteredClientUtils::jwsAlgorithm)
                .to(builder::tokenEndpointAuthenticationSigningAlgorithm);
        return builder.build();
    }

    private static TokenSettings getTokenSettings(OAuth2AuthorizationServerProperties.Client client, PropertyMapper map) {
        OAuth2AuthorizationServerProperties.Token token = client.getToken();
        TokenSettings.Builder builder = TokenSettings.builder();
        map.from(token::getAuthorizationCodeTimeToLive).to(builder::authorizationCodeTimeToLive);
        map.from(token::getAccessTokenTimeToLive).to(builder::accessTokenTimeToLive);
        map.from(token::getAccessTokenFormat).as(OAuth2TokenFormat::new).to(builder::accessTokenFormat);
        map.from(token::getDeviceCodeTimeToLive).to(builder::deviceCodeTimeToLive);
        map.from(token::isReuseRefreshTokens).to(builder::reuseRefreshTokens);
        map.from(token::getRefreshTokenTimeToLive).to(builder::refreshTokenTimeToLive);
        map.from(token::getIdTokenSignatureAlgorithm)
                .as(OAuth2RegisteredClientUtils::signatureAlgorithm)
                .to(builder::idTokenSignatureAlgorithm);
        return builder.build();
    }

    private static JwsAlgorithm jwsAlgorithm(String signingAlgorithm) {
        String name = signingAlgorithm.toUpperCase();
        JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.from(name);
        if (jwsAlgorithm == null) {
            jwsAlgorithm = MacAlgorithm.from(name);
        }
        return jwsAlgorithm;
    }

    private static SignatureAlgorithm signatureAlgorithm(String signatureAlgorithm) {
        return SignatureAlgorithm.from(signatureAlgorithm.toUpperCase());
    }

}
