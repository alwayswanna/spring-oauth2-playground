package a.gleb.oauth2server.mapper

import a.gleb.oauth2server.db.entity.Account
import a.gleb.oauth2server.model.AccountRequest
import a.gleb.oauth2server.model.AccountResponse
import org.springframework.stereotype.Component

/**
 * [Account] entity mapper.
 * Map entity to response DTO [AccountResponse];
 * Map request DTO [AccountRequest] to entity;
 */
@Component
class AccountMapper {

    fun toEntity(request: AccountRequest): Account {
        return Account(
            null,
            request.login,
            emptyList(),
            false
        )
    }

    fun toResponse(savedAccount: Account): AccountResponse {
        return AccountResponse(
            savedAccount.id!!,
            savedAccount.preferredUsername,
            savedAccount.roles.asSequence().map { it.roleName }.toList(),
            savedAccount.disabled
        )
    }
}