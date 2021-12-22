package com.mercadolivro.exception.response

data class FieldErrorResponse(
    var message: String,
    var field: String,
)
