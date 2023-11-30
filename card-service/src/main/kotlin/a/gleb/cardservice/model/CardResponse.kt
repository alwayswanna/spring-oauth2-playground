package a.gleb.cardservice.model

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

@Schema(description = "Card response model")
data class CardResponse(

    @Schema(description = "ID of card entity")
    val cardId: UUID?,

    @Schema(description = "Card title")
    val cardTitle: String,

    @Schema(description = "Card description (optional)")
    val cardDescription: String?,

    @Schema(description = "Card owned username")
    val sub: String,

    @Schema(description = "Last update time for card")
    val updateTime: LocalDateTime
)
