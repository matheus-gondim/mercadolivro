package com.mercadolivro.service

import com.mercadolivro.controller.mapper.PurchaseMapper
import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.mocks.PurchaseMock
import com.mercadolivro.repository.PurchaseRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import java.util.*

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {
    @MockK
    private lateinit var mapper: PurchaseMapper

    @MockK
    private lateinit var repository: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    private val purchaseEventSlot = slot<PurchaseEvent>()

    @Test
    fun `should create purchase and publish event`() {
        val id = Random().nextInt()
        val customerId = Random().nextInt()
        val createPurchaseMock = PurchaseMock.buildCreatePurchaseDto(customerId)
        val purchaseMock = PurchaseMock.buildPurchase(id)

        every { mapper.toModel(createPurchaseMock) } returns purchaseMock
        every { repository.save(purchaseMock) } returns purchaseMock
        every { applicationEventPublisher.publishEvent(any()) } just runs

        purchaseService.create(createPurchaseMock)

        verify(exactly = 1) { repository.save(purchaseMock) }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }

        assertEquals(purchaseMock, purchaseEventSlot.captured.purchase)
    }

    @Test
    fun `should update purchase`() {
        val id = Random().nextInt()
        val purchaseMock = PurchaseMock.buildPurchase(id)

        every { repository.save(purchaseMock) } returns purchaseMock

        purchaseService.update(purchaseMock)

        verify(exactly = 1) { repository.save(purchaseMock) }
    }
}