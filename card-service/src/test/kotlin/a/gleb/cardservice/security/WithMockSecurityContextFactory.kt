package a.gleb.cardservice.security

import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.test.context.support.WithSecurityContextFactory

const val SUB_CLAIM = "sub"
const val MUST_BE_UNUSED = "must-be-unused"
const val DEFAULT_ROLE = "uma_authorization"

class WithMockSecurityContextFactory : WithSecurityContextFactory<WithJwtUser> {

    override fun createSecurityContext(annotation: WithJwtUser?): SecurityContext {
        val context = SecurityContextHolder.createEmptyContext()

        val jwt = Jwt.withTokenValue(MUST_BE_UNUSED)
            .header(MUST_BE_UNUSED, MUST_BE_UNUSED)
            .claim(SUB_CLAIM, annotation?.username)
            .issuer("http://127.0.0.1:9001")
            .build()

        context.authentication = JwtAuthenticationToken(jwt, listOf(SimpleGrantedAuthority(DEFAULT_ROLE)))
        return context
    }
}