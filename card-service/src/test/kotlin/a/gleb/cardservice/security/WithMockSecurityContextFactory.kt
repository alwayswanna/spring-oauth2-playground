package a.gleb.cardservice.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.test.context.support.WithSecurityContextFactory

const val USER_ID_CLAIM_VALUE = "c5a7f1e2-760e-4fb5-961a-00112ec1358c"
const val USER_ID_CLAIM = "user_id"
const val MUST_BE_UNUSED = "must-be-unused"
const val ROLE_CLAIM = "role"
const val DEFAULT_ROLE = "ROLE_ADMIN"

class WithMockSecurityContextFactory : WithSecurityContextFactory<WithJwtUser> {

    override fun createSecurityContext(annotation: WithJwtUser?): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()

        val jwt = Jwt.withTokenValue(MUST_BE_UNUSED)
            .header(MUST_BE_UNUSED, MUST_BE_UNUSED)
            .claim(USER_ID_CLAIM, USER_ID_CLAIM_VALUE)
            .claim(ROLE_CLAIM, listOf(DEFAULT_ROLE))
            .issuer("http://127.0.0.1:9001")
            .build()

        context.authentication = JwtAuthenticationToken(jwt, listOf(SimpleGrantedAuthority(DEFAULT_ROLE)))
        return context
    }
}