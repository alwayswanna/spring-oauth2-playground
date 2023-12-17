package a.gleb.statisticsservice.scheduler

import a.gleb.statisticsservice.logger
import a.gleb.statisticsservice.service.StatisticsService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class StatisticsScheduler(
    private val statisticsService: StatisticsService
) {

    @Scheduled(cron = "\${statistics-service.schedulers.cards}")
    fun loadStatistics() {
        logger.info { "Start load statistic scheduled operation" }

        try {
            statisticsService.saveStatistic()
        } catch (e: Exception) {
            logger.error { "Error while load statistic: ${e.message}" }
        }

        logger.info { "Finish load statistic scheduled operation" }
    }

}