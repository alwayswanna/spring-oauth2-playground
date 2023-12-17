package a.gleb.oauth2server.model

import java.util.*

data class AccountRequest(
    val id: UUID?,
    val login: String,
    val roleIds: List<UUID>
)
