package a.gleb.keycloaktojwt.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@Getter
@Setter
@ConfigurationProperties("oauth2-server")
public class KeycloakToJwtConfigurationProperties {

    private CorsConfig cors;

    @Getter
    @Setter
    public static class CorsConfig extends CorsConfiguration {
    }
}

