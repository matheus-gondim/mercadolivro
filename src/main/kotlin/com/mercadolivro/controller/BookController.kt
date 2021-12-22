package com.mercadolivro.controller


import com.mercadolivro.extension.toResponse
import com.mercadolivro.controller.request.CreateBookDto
import com.mercadolivro.controller.request.UpdateBookDto
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.service.BookService
import org.springdoc.api.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("books")
class BookController(
    private val service: BookService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid dto: CreateBookDto) {
        service.create(dto)
    }

    @GetMapping
    fun find(
        @ParameterObject @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): Page<BookResponse> {
        return service.find(pageable).map { it.toResponse() }
    }

    @GetMapping("/active")
    fun findActives(
        @ParameterObject @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): Page<BookResponse> {
        return service.findActives(pageable).map { it.toResponse() }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): BookResponse {
        return service.findById(id).toResponse()
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable id: Int,
        @RequestBody @Valid dto: UpdateBookDto,
    ) {
        service.update(id, dto)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        service.delete(id)
    }
}