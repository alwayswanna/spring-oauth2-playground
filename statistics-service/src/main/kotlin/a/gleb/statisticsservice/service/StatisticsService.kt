package a.gleb.statisticsservice.service

import a.gleb.statisticsservice.db.entity.StatisticsEntity
import a.gleb.statisticsservice.db.repository.StatisticsEntityRepository
import a.gleb.statisticsservice.feign.CardFeignClient
import a.gleb.statisticsservice.logger
import a.gleb.statisticsservice.model.CardResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class StatisticsService(
    private val cardFeignClient: CardFeignClient,
    private val statisticsEntityRepository: StatisticsEntityRepository
) {

    fun saveStatistic() {
        logger.info { "Start load statistic" }
        val statistics = cardFeignClient.statistics()

        if (statistics.isEmpty()) {
            return
        }

        logger.info { "Statistic loaded, [size=${statistics.size}]" }
        val pairStatistics = statistics.asSequence()
            .map { it.sub to it }
            .toMap()

        pairStatistics.keys.asSequence()
            .map {
                StatisticsEntity(null, it, count(it, statistics), LocalDateTime.now())
            }
            .forEach {
                logger.info { "Save statistic to database." }
                statisticsEntityRepository.save(it)
            }
    }

    private fun count(sub: String, statistics: List<CardResponse>): Int {
        return statistics.count { it.sub == sub }
    }
}