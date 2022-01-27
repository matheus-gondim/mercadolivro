package com.mercadolivro.service


import com.mercadolivro.controller.request.CreateCustomerDto
import com.mercadolivro.controller.request.UpdateCustomerDto
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.extension.toCustomer
import com.mercadolivro.model.entity.Customer
import com.mercadolivro.model.enum.CustomerStatus
import com.mercadolivro.model.enum.Errors
import com.mercadolivro.model.enum.Role
import com.mercadolivro.repository.CustomerRepository
import org.springframework.context.annotation.Lazy
import org.springframework.lang.NonNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class CustomerService(
    private val repository: CustomerRepository,
    private val bookService: BookService,
    private val bCrypt: BCryptPasswordEncoder
) {

    fun find(name: String?): List<Customer> {
        name?.let {
            return repository.findByNameContainingIgnoreCase(name)
        }
        return repository.findAll().toList()
    }

    fun findById(id: Int): Customer {
        return repository.findById(id).orElseThrow {
            NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code)
        }
    }

    fun create(dto: CreateCustomerDto) {
        val customer = dto.toCustomer()
        customer.roles = setOf(Role.CUSTOMER)
        customer.password = bCrypt.encode(customer.password)
        repository.save(customer)
    }

    fun update(id: Int, dto: UpdateCustomerDto) {
        val customerSaved = this.findById(id)
        val customer = dto.toCustomer(customerSaved)
        repository.save(customer)
    }

    @Transactional
    fun delete(id: Int) {
        val customer = findById(id)
        bookService.deleteByCustomer(customer)
        customer.status = CustomerStatus.INATIVO
        repository.save(customer)
    }

    fun emailAvailable(value: String): Boolean {
        return !repository.existsByEmail(value)
    }
}