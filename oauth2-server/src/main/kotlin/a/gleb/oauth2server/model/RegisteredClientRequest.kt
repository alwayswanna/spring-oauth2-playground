package a.gleb.oauth2server.model

import java.util.*

data class RegisteredClientRequest(
    val id: UUID?,
    val clientId: String?,
    val clientSecret: String?,
    val clientAuthorizationGrantTypes: List<String>?,
    val clientAuthenticationMethods: List<String>?,
    val clientRedirectUris: List<String>?,
    val clientPostLogoutUris: List<String>?,
    val scopes: List<String>?,
)