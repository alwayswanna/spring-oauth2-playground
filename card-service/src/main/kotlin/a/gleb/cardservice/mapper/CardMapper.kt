package a.gleb.cardservice.mapper

import a.gleb.cardservice.db.entity.CardEntity
import a.gleb.cardservice.logger
import a.gleb.cardservice.model.CardRequest
import a.gleb.cardservice.model.CardResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CardMapper {

    fun toResponse(entity: CardEntity): CardResponse {
        logger.info { "Map entity to response: [entity.id=$entity.id]" }
        return with(entity) {
            CardResponse(id, title, description, sub, lastUpdate)
        }
    }

    fun toCardEntity(request: CardRequest, userSub: String): CardEntity {
        logger.info { "Map request to entity: [request.title=${request.title}, sub=$userSub]" }
        return CardEntity(null, request.title, request.description, userSub, LocalDateTime.now())
    }
}