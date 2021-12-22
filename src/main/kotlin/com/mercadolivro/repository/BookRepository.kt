package com.mercadolivro.repository

import com.mercadolivro.model.entity.Book
import com.mercadolivro.model.entity.Customer
import com.mercadolivro.model.enum.BookStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Int> {
    fun findByStatus(status: BookStatus, pageable: Pageable): Page<Book>
    fun findByCustomer(customer: Customer): List<Book>
}