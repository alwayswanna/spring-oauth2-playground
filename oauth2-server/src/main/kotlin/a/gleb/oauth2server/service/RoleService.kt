package a.gleb.oauth2server.service

import a.gleb.oauth2server.db.repository.RoleRepository
import a.gleb.oauth2server.exception.BadRequestException
import a.gleb.oauth2server.exception.RoleNotFoundException
import a.gleb.oauth2server.logger
import a.gleb.oauth2server.mapper.RoleRequestMapper
import a.gleb.oauth2server.model.RoleRequest
import a.gleb.oauth2server.model.RoleResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class RoleService(
    private val roleRepository: RoleRepository,
    private val roleRequestMapper: RoleRequestMapper
) {

    /**
     * Create new role.
     */
    fun create(request: RoleRequest): RoleResponse {
        logger.info { "Create new role, with name=${request.roleName}" }
        val entity = roleRepository.save(roleRequestMapper.toEntity(request))
        return roleRequestMapper.toResponse(entity)
    }

    /**
     * Delete role by id.
     */
    fun delete(id: UUID) {
        logger.info { "Delete role with id=$id" }
        roleRepository.deleteById(id)
        logger.info { "Successfully delete role with id=$id" }
    }

    /**
     * Get all roles.
     */
    fun getRoles(): List<RoleResponse> {
        logger.info { "Search all roles." }
        return roleRepository.findAll()
            .map(roleRequestMapper::toResponse)
            .toList()
    }

    /**
     * Update role.
     */
    fun update(request: RoleRequest): RoleResponse {
        logger.info { "Attempt to update role with id=${request.id}" }
        if (request.id == null) {
            throw BadRequestException("Can`t change role when request.id is NULL.")
        }

        val role = roleRepository.findById(request.id)
            .orElseThrow { RoleNotFoundException("Role with id=${request.id} does not exists") }
        val entityToSave = roleRequestMapper.toEntity(request)
        entityToSave.id = request.id
        entityToSave.accounts = role.accounts

        return roleRequestMapper.toResponse(roleRepository.save(entityToSave))
    }
}
