package a.gleb.oauth2server

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

val logger = KotlinLogging.logger {}

@SpringBootApplication
class Oauth2ServerApplication

fun main(args: Array<String>) {
    runApplication<Oauth2ServerApplication>(*args)
}
