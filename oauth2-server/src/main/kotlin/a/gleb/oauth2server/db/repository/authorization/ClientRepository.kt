package a.gleb.oauth2server.db.repository.authorization

import a.gleb.oauth2server.db.entity.authorization.Client
import org.springframework.data.jpa.repository.JpaRepository

interface ClientRepository : JpaRepository<Client, String> {

    fun findByClientId(clientId: String): Client?
}