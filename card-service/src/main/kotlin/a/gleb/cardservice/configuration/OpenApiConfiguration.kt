package a.gleb.cardservice.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

const val OAUTH2_SECURITY_SCHEMA = "myOauth2Security"

@OpenAPIDefinition(
    info = Info(
        title = "card-manager-backend",
        description = "API service for manage cards entities", version = "1"
    )
)
@SecurityScheme(
    name = OAUTH2_SECURITY_SCHEMA,
    type = SecuritySchemeType.OPENIDCONNECT,
    openIdConnectUrl = "\${springdoc.oAuthFlow.oidcEndpoint}"
)
@Configuration
class OpenApiConfiguration