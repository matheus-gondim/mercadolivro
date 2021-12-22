package com.mercadolivro.extension

import com.mercadolivro.controller.request.CreateBookDto
import com.mercadolivro.controller.request.UpdateBookDto
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.controller.request.CreateCustomerDto
import com.mercadolivro.controller.request.UpdateCustomerDto
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.model.entity.Book
import com.mercadolivro.model.entity.Customer
import com.mercadolivro.model.enum.BookStatus
import com.mercadolivro.model.enum.CustomerStatus


fun CreateCustomerDto.toCustomer(): Customer {
    return Customer(
        name = this.name,
        email = this.email,
        status = CustomerStatus.ATIVO,
        password = this.password
    )
}

fun UpdateCustomerDto.toCustomer(previousValue: Customer): Customer {
    return Customer(
        id = previousValue.id,
        name = this.name,
        email = this.email,
        status = previousValue.status,
        password = previousValue.password
    )
}

fun CreateBookDto.toBook(customer: Customer): Book {
    return Book(
        name = this.name,
        price = this.price,
        status = BookStatus.ATIVO,
        customer = customer
    )
}

fun UpdateBookDto.toBook(previousValue: Book): Book {
    return Book(
        id = previousValue.id,
        name = this.name ?: previousValue.name,
        price = this.price ?: previousValue.price,
        status = previousValue.status,
        customer = previousValue.customer,
    )
}

fun Customer.toResponse(): CustomerResponse {
    return CustomerResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        status = this.status
    )
}

fun Book.toResponse(): BookResponse {
    return BookResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        customer = this.customer,
        status = this.status
    )
}