package a.gleb.cardservice

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val logger = KotlinLogging.logger {}

@SpringBootApplication
class CardServiceApplication

fun main(args: Array<String>) {
    runApplication<CardServiceApplication>(*args)
}
