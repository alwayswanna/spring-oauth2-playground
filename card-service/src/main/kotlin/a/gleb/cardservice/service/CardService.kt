package a.gleb.cardservice.service

import a.gleb.cardservice.db.repository.CardEntityRepository
import a.gleb.cardservice.exceptions.BadRequestException
import a.gleb.cardservice.exceptions.CardAccessDenied
import a.gleb.cardservice.exceptions.CardNotFoundException
import a.gleb.cardservice.logger
import a.gleb.cardservice.mapper.CardMapper
import a.gleb.cardservice.model.CardRequest
import a.gleb.cardservice.model.CardResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class CardService(
    private val cardMapper: CardMapper,
    private val cardEntityRepository: CardEntityRepository,
    private val securityContextService: SecurityContextService
) {

    /**
     * Method for get card entity by ID.
     * @param id request param from HTTP-request.
     */
    fun get(id: UUID): CardResponse {
        logger.info { "Search card with [id=$id]" }
        val cardEntity =
            cardEntityRepository.findByIdOrNull(id) ?: throw CardNotFoundException("Card with $id is not found")

        return cardMapper.toResponse(cardEntity)
    }

    /**
     * Method for create new card.
     * @param request body from HTTP-request.
     */
    fun create(request: CardRequest): CardResponse {
        logger.info { "Create card with data: [title=${request.title}, description=${request.description}]" }
        val userSub = securityContextService.getUserSub()
        val entity = cardMapper.toCardEntity(request, userSub)
        val saveEntity = cardEntityRepository.save(entity)

        return cardMapper.toResponse(saveEntity)
    }

    /**
     * Method for update card entity.
     * @param request body from HTTP-request.
     */
    fun update(request: CardRequest): CardResponse {
        logger.info { "Update card with data: [id=${request.id}, title=${request.title}, description=${request.description}]" }
        val id = request.id ?: throw BadRequestException("ID must be set for update")

        val entity =
            cardEntityRepository.findByIdOrNull(id) ?: throw CardNotFoundException("Card with $id is not found")
        val sub = securityContextService.getUserSub()

        if (sub != entity.sub) {
            throw CardAccessDenied("You do not own this card")
        }

        entity.description = request.description
        entity.title = request.title
        entity.lastUpdate = LocalDateTime.now()
        val saveEntity = cardEntityRepository.save(entity)

        return cardMapper.toResponse(saveEntity)
    }

    /**
     * Method for delete existing card.
     * @param id request param from HTTP-request.
     */
    fun delete(id: UUID) {
        logger.info { "Remove card with [id=$id]" }
        val entity =
            cardEntityRepository.findByIdOrNull(id) ?: throw CardNotFoundException("Card with $id is not found")
        val sub = securityContextService.getUserSub()

        if (sub != entity.sub) {
            throw CardAccessDenied("You do not own this card")
        }

        cardEntityRepository.delete(entity)
    }

    /**
     * Method for load statistic for cards.
     */
    fun statistics(): List<CardResponse> {
        logger.info { "Load all cards for statistic request" }
        return cardEntityRepository.findAll()
            .map { cardMapper.toResponse(it) }
            .toList()
    }
}