package com.mercadolivro.repository

import com.mercadolivro.model.entity.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: CrudRepository<Customer, Int> {
    fun findByNameContainingIgnoreCase(name: String): List<Customer>
    fun existsByEmail(value: String): Boolean
    fun findByEmail(email: String): Customer?
}