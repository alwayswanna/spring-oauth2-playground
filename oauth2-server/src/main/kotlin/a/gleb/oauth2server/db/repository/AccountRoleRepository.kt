package a.gleb.oauth2server.db.repository

import a.gleb.oauth2server.db.entity.AccountRole
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRoleRepository : JpaRepository<AccountRole, UUID>