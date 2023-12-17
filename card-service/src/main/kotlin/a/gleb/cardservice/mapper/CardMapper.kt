package a.gleb.cardservice.mapper

import a.gleb.cardservice.db.entity.CardEntity
import a.gleb.cardservice.logger
import a.gleb.cardservice.model.CardRequest
import a.gleb.cardservice.model.CardResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class CardMapper {

    fun toResponse(entity: CardEntity): CardResponse {
        logger.info { "Map entity to response: [entity.id=$entity.id]" }
        return with(entity) {
            CardResponse(id, title, description, userId, lastUpdate)
        }
    }

    fun toCardEntity(request: CardRequest, userId: UUID): CardEntity {
        logger.info { "Map request to entity: [request.title=${request.title}, sub=$userId]" }
        return CardEntity(null, request.title, request.description, userId, LocalDateTime.now())
    }
}