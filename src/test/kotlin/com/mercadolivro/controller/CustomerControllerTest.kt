package com.mercadolivro.controller


import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.mocks.CustomerMocks
import com.mercadolivro.model.enum.CustomerStatus
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.UserSecurity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should return all customers when get all`() {
        val customer1 = customerRepository.save(CustomerMocks.buildCustomer())
        val customer2 = customerRepository.save(CustomerMocks.buildCustomer())

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(customer1.id))
            .andExpect(jsonPath("$[0].name").value(customer1.name))
            .andExpect(jsonPath("$[0].email").value(customer1.email))
            .andExpect(jsonPath("$[0].status").value(customer1.status?.name))
            .andExpect(jsonPath("$[1].id").value(customer2.id))
            .andExpect(jsonPath("$[1].name").value(customer2.name))
            .andExpect(jsonPath("$[1].email").value(customer2.email))
            .andExpect(jsonPath("$[1].status").value(customer2.status?.name))
    }

    @Test
    fun `should filter all customers by name when get all`() {
        val customer1 = customerRepository.save(CustomerMocks.buildCustomer(name = "Gustavo"))
        customerRepository.save(CustomerMocks.buildCustomer(name = "Daniel"))

        mockMvc.perform(get("/customers?name=Gus"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(customer1.id))
            .andExpect(jsonPath("$[0].name").value(customer1.name))
            .andExpect(jsonPath("$[0].email").value(customer1.email))
            .andExpect(jsonPath("$[0].status").value(customer1.status?.name))
    }

    @Test
    fun `should create customer`() {
        val request = CustomerMocks.buildCreateCustomerDto()
        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)

        val customers = customerRepository.findAll().toList()
        assertEquals(1, customers.size)
        assertEquals(request.name, customers[0].name)
        assertEquals(request.email, customers[0].email)
    }

    @Test
    fun `should throw error when create customer has invalid information`() {
        val request = CustomerMocks.buildCreateCustomerDto(password = "")
        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid Request"))
            .andExpect(jsonPath("$.internalCode").value("ML-001"))
    }

    @Test
    fun `should get user by id when user has the same id`() {
        val customer = customerRepository.save(CustomerMocks.buildCustomer())

        mockMvc.perform(get("/customers/${customer.id}").with(user(UserSecurity(customer))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status?.name))
    }

    @Test
    fun `should return forbidden when user has diffent id`() {
        val customer = customerRepository.save(CustomerMocks.buildCustomer(id = 2))

        mockMvc.perform(get("/customers/0").with(user(UserSecurity(customer))))
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Access Denied"))
            .andExpect(jsonPath("$.internalCode").value("ML-000"))
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should get user by id when user is admin`() {
        val customer = customerRepository.save(CustomerMocks.buildCustomer())

        mockMvc.perform(get("/customers/${customer.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status?.name))
    }

    @Test
    fun `should update customer`() {
        val customer = customerRepository.save(CustomerMocks.buildCustomer())
        val request = CustomerMocks.buildUpdateCustomerDto("Gustavo", "emailupdate@email.com")

        mockMvc.perform(
            put("/customers/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNoContent)

        val customers = customerRepository.findAll().toList()
        assertEquals(1, customers.size)
        assertEquals(request.name, customers[0].name)
        assertEquals(request.email, customers[0].email)

    }

    @Test
    fun `should return not found when update customer not existing`() {
        val request = CustomerMocks.buildUpdateCustomerDto("Gustavo", "emailupdate@email.com")

        mockMvc.perform(
            put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer [1] not exists"))
            .andExpect(jsonPath("$.internalCode").value("ML-201"))
    }

    @Test
    fun `should throw error when update customer has invalid information`() {
        val request = CustomerMocks.buildUpdateCustomerDto("", "emailupdate@email.com")
        mockMvc.perform(
            put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid Request"))
            .andExpect(jsonPath("$.internalCode").value("ML-001"))
    }

    @Test
    fun `should delete customer`() {
        val customer = customerRepository.save(CustomerMocks.buildCustomer())
        mockMvc.perform(delete("/customers/${customer.id}"))
            .andExpect(status().isNoContent)

        val customerDeleted = customerRepository.findById(customer.id!!)
        assertEquals(CustomerStatus.INATIVO, customerDeleted.get().status)
    }

    @Test
    fun `should return not found when delete customer not exists`() {
        mockMvc.perform(delete("/customers/1"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer [1] not exists"))
            .andExpect(jsonPath("$.internalCode").value("ML-201"))
    }

}