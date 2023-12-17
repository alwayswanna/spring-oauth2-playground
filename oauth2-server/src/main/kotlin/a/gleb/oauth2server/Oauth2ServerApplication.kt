package a.gleb.oauth2server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Oauth2ServerApplication

fun main(args: Array<String>) {
    runApplication<Oauth2ServerApplication>(*args)
}
