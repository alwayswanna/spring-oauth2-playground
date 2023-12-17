package a.gleb.statisticsservice.model

import java.time.LocalDateTime
import java.util.*

data class CardResponse(
    val cardId: UUID?,
    val cardTitle: String,
    val cardDescription: String?,
    val sub: String,
    val updateTime: LocalDateTime
)
