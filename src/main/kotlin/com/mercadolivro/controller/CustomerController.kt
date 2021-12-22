package com.mercadolivro.controller


import com.mercadolivro.extension.toResponse
import com.mercadolivro.controller.request.CreateCustomerDto
import com.mercadolivro.controller.request.UpdateCustomerDto
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("customers")
class CustomerController(
    private val service: CustomerService
) {

    @GetMapping
    fun find(@RequestParam name: String?): List<CustomerResponse> {
        return service.find(name).map { it.toResponse() }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Int): CustomerResponse {
        return service.findById(id).toResponse()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid dto: CreateCustomerDto) {
        service.create(dto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(
        @PathVariable id: Int,
        @RequestBody dto: UpdateCustomerDto,
    ) {
        service.update(id, dto)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        service.delete(id)
    }

}