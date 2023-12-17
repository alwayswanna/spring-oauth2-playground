package a.gleb.cardservice.service

import a.gleb.cardservice.exceptions.CardAccessDenied
import a.gleb.cardservice.logger
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import java.util.*

const val USER_SUB_CLAIM = "user_id"

@Service
class SecurityContextService {

    fun getUserId(): UUID{
        logger.info { "Load user sub param from token" }
        val principal: Jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val any = principal.claims[USER_SUB_CLAIM]?:
            throw CardAccessDenied("$USER_SUB_CLAIM attribute does not exist in token")

        return UUID.fromString(any as String)
    }
}