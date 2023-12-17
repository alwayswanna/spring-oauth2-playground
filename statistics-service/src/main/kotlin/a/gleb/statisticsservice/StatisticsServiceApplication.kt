package a.gleb.statisticsservice

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val logger = KotlinLogging.logger {}

@SpringBootApplication
class StatisticsServiceApplication

fun main(args: Array<String>) {
    runApplication<StatisticsServiceApplication>(*args)
}
