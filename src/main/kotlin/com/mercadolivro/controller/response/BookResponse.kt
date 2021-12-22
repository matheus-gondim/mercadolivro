package com.mercadolivro.controller.response

import com.mercadolivro.model.entity.Customer
import com.mercadolivro.model.enum.BookStatus
import java.math.BigDecimal

data class BookResponse(
    var id: Int? = null,
    var name: String,
    var price: BigDecimal,
    var customer: Customer? = null,
    var status: BookStatus? = null
)