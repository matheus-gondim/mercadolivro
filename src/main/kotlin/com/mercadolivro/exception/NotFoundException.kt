package com.mercadolivro.exception

import org.springframework.http.HttpStatus

class NotFoundException(
    override val message: String,
    val errorCode: String,
): Exception() {
    val httpCode = HttpStatus.NOT_FOUND
}