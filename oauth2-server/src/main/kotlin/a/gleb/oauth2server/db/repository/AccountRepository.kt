package a.gleb.oauth2server.db.repository

import a.gleb.oauth2server.db.entity.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface AccountRepository: JpaRepository<Account, UUID> {

    fun findAccountByUsername(username: String): Account?
}