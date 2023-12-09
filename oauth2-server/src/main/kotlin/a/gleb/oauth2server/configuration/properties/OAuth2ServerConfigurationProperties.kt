package a.gleb.oauth2server.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.cors.CorsConfiguration

@ConfigurationProperties("oauth2-server")
data class OAuth2ServerConfigurationProperties(
    var corsConfiguration: Cors
)


class Cors: CorsConfiguration()