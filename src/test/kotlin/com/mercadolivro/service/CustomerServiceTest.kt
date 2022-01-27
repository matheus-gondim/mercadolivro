package com.mercadolivro.service

import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.extension.toCustomer
import com.mercadolivro.mocks.CustomerMocks
import com.mercadolivro.model.enum.Errors
import com.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.ZonedDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {
    @MockK
    private lateinit var repository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK // => permite mockar metodos da class que esta sendo testada
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        val customersMock = listOf(CustomerMocks.buildCustomer(), CustomerMocks.buildCustomer())
        every { repository.findAll() } returns customersMock

        val customer = customerService.find(null)

        assertEquals(customersMock, customer)
        verify(exactly = 1) { repository.findAll() }
        verify(exactly = 0) { repository.findByNameContainingIgnoreCase(any()) }
    }

    @Test
    fun `should return customers when name is informed`() {
        val name = UUID.randomUUID().toString()
        val customersMock = listOf(CustomerMocks.buildCustomer(name = name))

        every { repository.findByNameContainingIgnoreCase(name) } returns customersMock

        val customer = customerService.find(name)

        assertEquals(customersMock, customer)
        verify(exactly = 1) { repository.findByNameContainingIgnoreCase(name) }
        verify(exactly = 0) { repository.findAll() }
    }

    @Test
    fun `should create customer and encrypt password`() {
        val initialPassword = ZonedDateTime.now().toInstant().toEpochMilli().toString()
        val passwordMock = UUID.randomUUID().toString()
        val customerMock = CustomerMocks.buildCustomer(password = passwordMock)
        val createCustomerMock = CustomerMocks.buildCreateCustomerDto(
            name = customerMock.name,
            email = customerMock.email,
            password = initialPassword
        )

        every { repository.save(customerMock) } returns customerMock
        every { bCrypt.encode(initialPassword) } returns passwordMock

        customerService.create(createCustomerMock)

        verify(exactly = 1) { repository.save(customerMock) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }
    }

    @Test
    fun `should return customer by id`() {
        val id = Random().nextInt()
        val customerMock = CustomerMocks.buildCustomer(id = id)

        every { repository.findById(id) } returns Optional.of(customerMock)

        val customer = customerService.findById(id)

        assertEquals(customerMock, customer)
        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `should throw not found when find by id`() {
        val id = Random().nextInt()
        every { repository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> { customerService.findById(id) }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("ML-201", error.errorCode)
        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun `should update customer`() {
        val id = Random().nextInt()
        val customerMock = CustomerMocks.buildCustomer(id = id)
        val mockDto = CustomerMocks.buildUpdateCustomerDto()
        val updateCustomerMock = mockDto.toCustomer(customerMock)

        every { customerService.findById(id) } returns customerMock
        every { repository.save(updateCustomerMock) } returns customerMock

        customerService.update(id, mockDto)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 1) { repository.save(updateCustomerMock) }
    }

    @Test
    fun `should delete customer`() {
        val id = Random().nextInt()
        val customerMock = CustomerMocks.buildCustomer(id = id)

        every { customerService.findById(id) } returns customerMock
        every { bookService.deleteByCustomer(customerMock) } just runs
        every { repository.save(customerMock) } returns customerMock

        customerService.delete(id)

        verify(exactly = 1) { bookService.deleteByCustomer(customerMock) }
        verify(exactly = 1) { repository.save(customerMock) }
    }

    @Test
    fun `should throw not found exception when delete customer`() {
        val id = Random().nextInt()

        every { customerService.findById(id) } throws NotFoundException(
            Errors.ML201.message.format(id),
            Errors.ML201.code
        )

        val err = assertThrows<NotFoundException> { customerService.delete(id) }

        assertEquals("Customer [${id}] not exists", err.message)
        assertEquals("ML-201", err.errorCode)
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `should return true when email available`() {
        val email = "${Random().nextInt()}@email.com"
        every { repository.existsByEmail(email) } returns false

        val result = customerService.emailAvailable(email)

        verify(exactly = 1) { repository.existsByEmail(email)  }
        assertTrue(result)
    }

    @Test
    fun `should return true when email unavailable`() {
        val email = "${Random().nextInt()}@email.com"
        every { repository.existsByEmail(email) } returns true

        val result = customerService.emailAvailable(email)

        verify(exactly = 1) { repository.existsByEmail(email)  }
        assertFalse(result)
    }

}