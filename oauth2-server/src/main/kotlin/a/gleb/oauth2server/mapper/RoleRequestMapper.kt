package a.gleb.oauth2server.mapper

import a.gleb.oauth2server.db.entity.Role
import a.gleb.oauth2server.model.RoleRequest
import a.gleb.oauth2server.model.RoleResponse
import org.springframework.stereotype.Component

/**
 * [Role] entity mapper.
 * Map entity to response DTO [RoleResponse];
 * Map request DTO [RoleRequest] to entity;
 */
@Component
class RoleRequestMapper {

    fun toResponse(role: Role): RoleResponse {
        return RoleResponse(role.id!!, role.roleName)
    }

    fun toEntity(request: RoleRequest): Role {
        return Role(id = request.id, roleName = request.roleName, setOf())
    }
}