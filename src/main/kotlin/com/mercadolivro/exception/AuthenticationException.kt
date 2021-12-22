package com.mercadolivro.exception

import org.springframework.http.HttpStatus

class AuthenticationException(
    override val message: String,
    val errorCode: String,
) : Exception() {
    val httpCode = HttpStatus.FORBIDDEN
}