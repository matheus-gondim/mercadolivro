package com.mercadolivro.service

import com.mercadolivro.controller.mapper.PurchaseMapper
import com.mercadolivro.controller.request.CreatePurchaseDto
import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.model.entity.Purchase
import com.mercadolivro.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val mapper: PurchaseMapper,
    private val repository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    fun create(dto: CreatePurchaseDto) {
        val purchase = mapper.toModel(dto)
        repository.save(purchase)
        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchase))
    }

    fun update(purchase: Purchase) {
        repository.save(purchase)
    }
}