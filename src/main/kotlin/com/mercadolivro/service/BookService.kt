package com.mercadolivro.service


import com.mercadolivro.controller.request.CreateBookDto
import com.mercadolivro.controller.request.UpdateBookDto
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.extension.toBook
import com.mercadolivro.model.entity.Book
import com.mercadolivro.model.entity.Customer
import com.mercadolivro.model.enum.BookStatus
import com.mercadolivro.model.enum.Errors
import com.mercadolivro.repository.BookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService(
    private val repository: BookRepository,
    private val customerService: CustomerService
) {
    fun create(dto: CreateBookDto) {
        val customer = customerService.findById(dto.customerId)
        val book = dto.toBook(customer)
        repository.save(book)
    }

    fun find(pageable: Pageable): Page<Book> {
        return repository.findAll(pageable)
    }

    fun findActives(pageable: Pageable): Page<Book> {
        return repository.findByStatus(BookStatus.ATIVO, pageable)
    }

    fun findById(id: Int): Book {
        return repository.findById(id).orElseThrow {
            NotFoundException(Errors.ML101.message.format(id), Errors.ML101.code)
        }
    }

    fun delete(id: Int) {
        val book = findById(id)
        book.status = BookStatus.CANCELADO
        repository.save(book)
    }

    fun update(id: Int, dto: UpdateBookDto) {
        val bookSaved = findById(id)
        val book = dto.toBook(bookSaved)
        repository.save(book)
    }

    fun deleteByCustomer(customer: Customer) {
        val books = repository.findByCustomer(customer)
        books.forEach { it.status = BookStatus.DELETADO }
        repository.saveAll(books)
    }

    fun findAllByIds(ids: Set<Int>): List<Book> {
        return repository.findAllById(ids).toList()
    }

    fun purchase(books: MutableList<Book>) {
        books.forEach {
            it.status = BookStatus.VENDIDO
        }
        repository.saveAll(books)
    }
}
