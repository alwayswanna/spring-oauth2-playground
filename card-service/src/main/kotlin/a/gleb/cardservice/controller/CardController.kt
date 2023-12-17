package a.gleb.cardservice.controller

import a.gleb.cardservice.configuration.OAUTH2_SECURITY_SCHEMA
import a.gleb.cardservice.model.CardRequest
import a.gleb.cardservice.model.CardResponse
import a.gleb.cardservice.service.CardService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

const val CARD_CONTROLLER = "card.controller"

@RestController
@RequestMapping("/api/v1/card")
@Tag(name = CARD_CONTROLLER)
class CardController(
    private val cardService: CardService
) {

    @Operation(
        summary = "Get card by ID",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @GetMapping
    fun get(@RequestParam id: UUID): CardResponse {
        return cardService.get(id)
    }

    @Operation(
        summary = "Create new card",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @PostMapping
    fun create(@RequestBody request: CardRequest): CardResponse {
        return cardService.create(request)
    }

    @Operation(
        summary = "Update card",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @PutMapping
    fun update(@RequestBody request: CardRequest): CardResponse {
        return cardService.update(request)
    }

    @Operation(
        summary = "Delete card",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @DeleteMapping
    fun delete(@RequestParam id: UUID) {
        return cardService.delete(id)
    }
}