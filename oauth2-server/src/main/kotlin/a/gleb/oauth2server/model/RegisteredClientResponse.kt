package a.gleb.oauth2server.model

import java.util.*

class RegisteredClientResponse (
    val id: UUID,
    val clientId: String,
    val encodedClientSecret: String,
    val clientAuthorizationGrantTypes: List<String>,
    val clientAuthenticationMethods: List<String>,
    val clientRedirectUris: List<String>,
    val clientPostLogoutUris: List<String>,
    val scopes: List<String>,
)