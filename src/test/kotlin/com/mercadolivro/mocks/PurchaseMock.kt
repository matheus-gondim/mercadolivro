package com.mercadolivro.mocks

import com.mercadolivro.controller.request.CreatePurchaseDto
import com.mercadolivro.model.entity.Book
import com.mercadolivro.model.entity.Customer
import com.mercadolivro.model.entity.Purchase
import java.math.BigDecimal
import java.util.*

class PurchaseMock {
    companion object {
        fun buildPurchase(
            id: Int? = null,
            customer: Customer = CustomerMocks.buildCustomer(),
            books: MutableList<Book> = mutableListOf(),
            nfe: String? = UUID.randomUUID().toString(),
            price: BigDecimal = BigDecimal.TEN
        ) = Purchase(
            id = id,
            customer = customer,
            books = books,
            nfe = nfe,
            price = price
        )

        fun buildCreatePurchaseDto(
            customerId: Int,
            bookIds: MutableSet<Int> = mutableSetOf()
        ) = CreatePurchaseDto(
            customerId = customerId,
            bookIds = bookIds
        )
    }
}