package a.gleb.cardservice.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_card")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "card_title", nullable = false)
    private String cardTitle;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "last_modified_date", nullable = false)
    private LocalDateTime lastModifiedDate;
}
