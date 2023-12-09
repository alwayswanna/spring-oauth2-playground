package a.gleb.oauth2server.db.repository

import a.gleb.oauth2server.db.entity.KeycloakAuthorizationSession
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface KeycloakAuthorizationSessionRepository: JpaRepository<KeycloakAuthorizationSession, UUID>