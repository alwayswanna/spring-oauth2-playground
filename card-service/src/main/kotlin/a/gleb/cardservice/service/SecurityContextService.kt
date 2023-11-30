package a.gleb.cardservice.service

import a.gleb.cardservice.logger
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

const val USER_SUB_CLAIM = "sub"

@Service
class SecurityContextService {

    fun getUserSub(): String{
        logger.info { "Load user sub param from token" }
        val principal: Jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val any = principal.claims[USER_SUB_CLAIM]?:
            throw AccessDeniedException("$USER_SUB_CLAIM attribute does not exist in token")

        return any as String
    }
}