package com.mercadolivro.controller

import com.mercadolivro.controller.request.CreatePurchaseDto
import com.mercadolivro.service.PurchaseService
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController("purchases")
class PurchaseController(
    private val service: PurchaseService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun purchase(@RequestBody @Valid dto: CreatePurchaseDto) {
        service.create(dto)
    }
}