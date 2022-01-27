package com.mercadolivro.mocks

import com.mercadolivro.controller.request.CreateCustomerDto
import com.mercadolivro.controller.request.UpdateCustomerDto
import com.mercadolivro.model.entity.Customer
import com.mercadolivro.model.enum.CustomerStatus
import com.mercadolivro.model.enum.Role
import java.util.*

class CustomerMocks {
    companion object {
        fun buildCustomer(
            id: Int? = null,
            name: String = "customer name",
            email: String = "${UUID.randomUUID()}@email.com",
            password: String = "password"
        ) = Customer(
            id = id,
            name = name,
            email = email,
            status = CustomerStatus.ATIVO,
            password = password,
            roles = setOf(Role.CUSTOMER)
        )

        fun buildCreateCustomerDto(
            name: String = "customer name",
            email: String = "${UUID.randomUUID()}@email.com",
            password: String = "password"
        ) = CreateCustomerDto(
            name = name,
            email = email,
            password = password,
        )

        fun buildUpdateCustomerDto(
            name: String = "update name",
            email: String = "${UUID.randomUUID()}@email.com",
        ) = UpdateCustomerDto(
            name = name,
            email = email,
        )
    }
}