package com.mercadolivro.controller.request

import java.math.BigDecimal

data class UpdateBookDto(
    var name: String?,
    var price: BigDecimal?,
)
