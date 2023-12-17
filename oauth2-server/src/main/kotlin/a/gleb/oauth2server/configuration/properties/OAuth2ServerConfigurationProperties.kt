package a.gleb.oauth2server.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.cors.CorsConfiguration

@ConfigurationProperties("oauth2-server")
data class OAuth2ServerConfigurationProperties(
    var corsConfiguration: Cors,
    val passwordEncoderStrength: Int,
    var securityConstraints: List<SecurityConstraints>
)

class Cors: CorsConfiguration()

data class SecurityConstraints(
    var roles: List<String>,
    var securityCollections: List<SecurityCollections>
)

data class SecurityCollections(
    var methods: List<String>?,
    var patterns: List<String>
)
