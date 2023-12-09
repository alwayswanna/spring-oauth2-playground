package a.gleb.oauth2server.db.repository

import a.gleb.oauth2server.db.entity.OAuth2AuthorizationInner
import org.springframework.data.jpa.repository.JpaRepository

interface OAuth2AuthorizationInnerRepository : JpaRepository<OAuth2AuthorizationInner, String> {

    fun findByPrincipalName(username: String): OAuth2AuthorizationInner?
}