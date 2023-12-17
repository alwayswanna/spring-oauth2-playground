package a.gleb.statisticsservice.db.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.AUTO
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

@Entity(name = "user_statistics")
data class StatisticsEntity(

    @Id
    @GeneratedValue(strategy = AUTO)
    var id: UUID?,

    @Column(name = "sub", nullable = false)
    var sub: String,

    @Column(name = "card_count", nullable = false)
    var cardCount: Int,

    @Column(name = "last_update", nullable = false)
    var lastUpdate: LocalDateTime
)
