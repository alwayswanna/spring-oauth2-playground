package a.gleb.cardservice.model

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "Card request model")
data class CardRequest(

    @Schema(description = "ID card for update.")
    val id: UUID?,

    @Schema(description = "Title of card.")
    val title: String,

    @Schema(description = "Description of card (optional).")
    val description: String?
)
