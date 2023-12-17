package a.gleb.oauth2server.controller

import a.gleb.oauth2server.configuration.OAUTH2_SECURITY_SCHEMA
import a.gleb.oauth2server.model.RegisteredClientRequest
import a.gleb.oauth2server.model.RegisteredClientResponse
import a.gleb.oauth2server.service.RegisteredClientService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

const val REGISTERED_CLIENT_CONTROLLER: String = "registered.client.controller"

@RestController
@RequestMapping("/api/v1/client")
@Tag(name = REGISTERED_CLIENT_CONTROLLER)
class RegisteredClientController(
    private val registeredClientService: RegisteredClientService
){

    @Operation(
        summary = "Create new registered client",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @PostMapping
    fun create(@RequestBody request: RegisteredClientRequest): RegisteredClientResponse {
        return registeredClientService.create(request)
    }

    @Operation(
        summary = "Delete client by id",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @DeleteMapping
    fun delete(@RequestParam id: UUID) {
        registeredClientService.delete(id)
    }

    @Operation(
        summary = "Get all registered clients",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @GetMapping
    fun getRegisteredClient(): List<RegisteredClientResponse> {
        return registeredClientService.getRegisteredClient()
    }

    @Operation(
        summary = "Update registered client",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @PutMapping
    fun update(@RequestBody request: RegisteredClientRequest): RegisteredClientResponse {
        return registeredClientService.update(request)
    }
}