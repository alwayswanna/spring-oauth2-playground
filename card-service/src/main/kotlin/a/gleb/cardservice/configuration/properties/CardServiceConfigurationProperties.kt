package a.gleb.cardservice.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.cors.CorsConfiguration

@ConfigurationProperties("card-service")
data class CardServiceConfigurationProperties(
    var corsConfiguration: Cors,
    var securityConstraints: List<SecurityConstraints>
)

class Cors: CorsConfiguration()

data class SecurityConstraints(
    var securityCollections: List<SecurityCollection>,
    var authRoles: List<String>
)

data class SecurityCollection(
    var patterns: List<String>,
    var methods: List<String>?
)
