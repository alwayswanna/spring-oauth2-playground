package a.gleb.oauth2server.controller

import a.gleb.oauth2server.configuration.OAUTH2_SECURITY_SCHEMA
import a.gleb.oauth2server.model.RoleRequest
import a.gleb.oauth2server.model.RoleResponse
import a.gleb.oauth2server.service.RoleService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

const val ROLE_CONTROLLER: String = "role.controller"

@RestController
@RequestMapping("/api/v1/role")
@Tag(name = ROLE_CONTROLLER)
class RoleRestController(
    private val roleService: RoleService
) {

    @Operation(
        summary = "Create new role",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @PostMapping
    fun create(@RequestBody request: RoleRequest): RoleResponse {
        return roleService.create(request)
    }

    @Operation(
        summary = "Delete role by id",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @DeleteMapping
    fun delete(@RequestParam id: UUID) {
        roleService.delete(id)
    }

    @Operation(
        summary = "Get all roles",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @GetMapping
    fun getRoles(): List<RoleResponse> {
        return roleService.getRoles()
    }

    @Operation(
        summary = "Update role",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @PutMapping
    fun update(@RequestBody request: RoleRequest): RoleResponse{
        return roleService.update(request)
    }
}