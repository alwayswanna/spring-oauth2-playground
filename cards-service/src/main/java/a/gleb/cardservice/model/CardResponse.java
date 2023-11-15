package a.gleb.cardservice.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse {

    private UUID cardId;
    private String cardTitle;
    private String description;
    private UUID userId;
    private LocalDateTime updatedTime;
}
