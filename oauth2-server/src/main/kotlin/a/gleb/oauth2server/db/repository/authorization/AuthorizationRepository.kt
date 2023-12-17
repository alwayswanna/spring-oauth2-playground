package a.gleb.oauth2server.db.repository.authorization

import a.gleb.oauth2server.db.entity.authorization.Authorization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AuthorizationRepository : JpaRepository<Authorization, String> {

    fun findByState(state: String?): Authorization?

    fun findByAuthorizationCodeValue(authorizationCode: String?): Authorization?

    fun findByAccessTokenValue(accessToken: String?): Authorization?

    fun findByRefreshTokenValue(refreshToken: String?): Authorization?

    fun findByOidcIdTokenValue(idToken: String?): Authorization?

    fun findByUserCodeValue(userCode: String?): Authorization?

    fun findByDeviceCodeValue(deviceCode: String?): Authorization?

    @Query(
        "select a from Authorization a where a.state = :token" +
                " or a.authorizationCodeValue = :token" +
                " or a.accessTokenValue = :token" +
                " or a.refreshTokenValue = :token" +
                " or a.oidcIdTokenValue = :token" +
                " or a.userCodeValue = :token" +
                " or a.deviceCodeValue = :token"
    )
    fun findByStateOrAuthorizationCodeValueOrAccessTokenValueOrRefreshTokenValueOrOidcIdTokenValueOrUserCodeValueOrDeviceCodeValue(
        @Param("token") token: String?
    ): Authorization?
}