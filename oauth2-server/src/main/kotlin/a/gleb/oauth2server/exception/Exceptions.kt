package a.gleb.oauth2server.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UserNotFoundException(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)

class RegisteredClientNotFoundException(message: String): ResponseStatusException(HttpStatus.NOT_FOUND, message)

class RoleNotFoundException(message: String) : ResponseStatusException(HttpStatus.NOT_FOUND, message)

class InvalidOAuth2OperationException(message: String): RuntimeException(message)

class NullableContextOAuth2Exception(message: String): RuntimeException(message)


class BadRequestException(message: String): ResponseStatusException(HttpStatus.BAD_REQUEST, message)