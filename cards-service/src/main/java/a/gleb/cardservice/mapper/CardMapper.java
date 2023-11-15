package a.gleb.cardservice.mapper;

import a.gleb.cardservice.db.entity.CardEntity;
import a.gleb.cardservice.model.CardResponse;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    /**
     * Mapper to response model.
     * @param entity from database.
     * @return {@link CardResponse} response to API.
     */
    public CardResponse toResponse(CardEntity entity) {
        return CardResponse.builder()
                .cardId(entity.getId())
                .cardTitle(entity.getCardTitle())
                .description(entity.getDescription())
                .userId(entity.getUserId())
                .updatedTime(entity.getLastModifiedDate())
                .build();
    }
}
