package a.gleb.cardservice.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.web.cors.CorsConfiguration

@ConfigurationProperties("card-service")
data class CardServiceConfigurationProperties(
    var corsConfiguration: Cors
)

class Cors: CorsConfiguration()
