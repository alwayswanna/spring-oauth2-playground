package a.gleb.cardservice.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.AUTO
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity(name = "user_card")
data class CardEntity(

    @Id
    @GeneratedValue(strategy = AUTO)
    var id: UUID?,

    @Column(name = "card_title", nullable = false)
    var title: String,

    @Column(name = "description", nullable = true)
    var description:  String?,

    @Column(name = "sub", nullable = false)
    var sub: String,

    @Column(name = "last_modified_date", nullable = false)
    var lastUpdate: LocalDateTime
)
