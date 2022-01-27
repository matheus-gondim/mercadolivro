package com.mercadolivro.events.listener

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.mocks.PurchaseMock
import com.mercadolivro.service.PurchaseService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class GenerateNfeListenerTest {
    @MockK
    private lateinit var purchaseService: PurchaseService

    @InjectMockKs
    private lateinit var generateNfeListener: GenerateNfeListener

    @Test
    fun `should generate NFE`() {
        val purchaseMock = PurchaseMock.buildPurchase(nfe = null)
        val nfeMock = UUID.randomUUID()
        val expectedPurchaseMock = purchaseMock.copy(nfe = nfeMock.toString())

        // serve para mockar classes estaticas
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns nfeMock;
        every { purchaseService.update(expectedPurchaseMock) } just runs

        generateNfeListener.listen(PurchaseEvent(this, purchaseMock))

        verify(exactly = 1) { purchaseService.update(expectedPurchaseMock) }
    }
}