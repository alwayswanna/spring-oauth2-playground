package a.gleb.oauth2server.db.repository

import a.gleb.oauth2server.db.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RoleRepository: JpaRepository<Role, UUID> {

    fun findAccountRoleByRoleName(roleName: String): Role?

    fun findRolesByIdIn(ids: List<UUID>): List<Role>?
}