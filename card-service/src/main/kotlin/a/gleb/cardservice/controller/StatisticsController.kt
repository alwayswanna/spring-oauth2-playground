package a.gleb.cardservice.controller

import a.gleb.cardservice.configuration.OAUTH2_SECURITY_SCHEMA
import a.gleb.cardservice.model.CardResponse
import a.gleb.cardservice.service.CardService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val STATISTIC_CONTROLLER = "statistics.controller"

@RestController
@RequestMapping("/api/v1/statistics")
@Tag(name = STATISTIC_CONTROLLER)
class StatisticsController (
    private val cardService: CardService
){

    @Operation(
        summary = "Load statistic by card.",
        security = [SecurityRequirement(name = OAUTH2_SECURITY_SCHEMA)]
    )
    @GetMapping
    fun statistics(): List<CardResponse> {
        return cardService.statistics()
    }
}