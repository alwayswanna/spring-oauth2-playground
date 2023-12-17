package a.gleb.oauth2server.model

import java.util.*

data class AccountResponse(
    val id: UUID,
    val login: String,
    val roleNames: List<String>,
    val disabled: Boolean
)
