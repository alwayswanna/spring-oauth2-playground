package a.gleb.oauth2server.controller

import a.gleb.oauth2server.configuration.OAUTH2_SECURITY_SCHEMA
import a.gleb.oauth2server.model.AccountRequest
import a.gleb.oauth2server.model.AccountResponse
import a.gleb.oauth2server.model.AccountSearchRequest
import a.gleb.oauth2server.service.AccountService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

const val USER_CONTROLLER: String = "account.controller"

@RestController
@RequestMapping("/api/v1/account")
@Tag(name = USER_CONTROLLER)
class UserRestController(
    private val accountService: AccountService
) {

    @Operation(
        summary = "Create user account",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @PostMapping
    fun create(@RequestBody request: AccountRequest): AccountResponse {
        return accountService.create(request)
    }

    @Operation(
        summary = "Disable user account by login",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @DeleteMapping
    fun delete(@RequestParam id: UUID) {
        accountService.delete(id)
    }

    @Operation(
        summary = "Get all accounts",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @GetMapping
    fun getAccounts(): List<AccountResponse> {
        return accountService.getAccounts()
    }

    @Operation(
        summary = "Update account",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @PutMapping
    fun update(@RequestBody request: AccountRequest): AccountResponse {
        return accountService.update(request)
    }

    @Operation(
        summary = "Search account by filter",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @GetMapping("/search")
    fun getByLogin(request: AccountSearchRequest): AccountResponse {
        return accountService.searchAccount(request)
    }
}