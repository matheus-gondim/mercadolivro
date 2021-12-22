package com.mercadolivro.validation.implementations

import com.mercadolivro.model.enum.BookStatus
import com.mercadolivro.service.BookService
import com.mercadolivro.validation.BooksAvailable
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class BooksAvailableValidator(
    private val bookService: BookService
) : ConstraintValidator<BooksAvailable, Set<Int>> {
    override fun isValid(value: Set<Int>?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return false
        val books = bookService.findAllByIds(value)
        return books.none { it.status != BookStatus.ATIVO }
    }
}