package a.gleb.cardservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CardNotFoundException(message: String): ResponseStatusException(HttpStatus.NOT_FOUND, message)

class BadRequestException(message: String): ResponseStatusException(HttpStatus.BAD_REQUEST, message)

class CardAccessDenied(message: String): ResponseStatusException(HttpStatus.FORBIDDEN, message)