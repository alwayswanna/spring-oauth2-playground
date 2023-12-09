package a.gleb.oauth2server.controller

import a.gleb.oauth2server.logger
import a.gleb.oauth2server.service.TokenService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth2/offer")
class TokenOfferController(
    private val tokenService: TokenService
) {

    @GetMapping
    fun offer(): Map<String, String> {
        logger.info { "Offer token Keycloak JWT -> Oauth2Server JWT" }
        return tokenService.offer()
    }
}