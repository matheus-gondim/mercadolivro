package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import com.mercadolivro.validation.BooksAvailable
import com.mercadolivro.validation.IsIds
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive


data class CreatePurchaseDto(
    @field:NotNull
    @field:Positive
    @JsonAlias("customer_id")
    val customerId: Int,

    @field:NotEmpty
    @BooksAvailable
    @IsIds
    @JsonAlias("book_ids")
    val bookIds: Set<Int>
)
