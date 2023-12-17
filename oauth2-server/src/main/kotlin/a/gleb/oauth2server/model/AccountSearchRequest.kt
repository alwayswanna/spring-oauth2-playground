package a.gleb.oauth2server.model

import java.util.*

data class AccountSearchRequest(
    val id: UUID?,
    val login: String?
)