package a.gleb.cardservice.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@Getter
@Setter
@ConfigurationProperties("card-service")
public class CardServiceConfigurationProperties {

    private Cors corsConfiguration;

    public static class Cors extends CorsConfiguration {}
}
