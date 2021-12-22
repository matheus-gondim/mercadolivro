package com.mercadolivro.service

import com.mercadolivro.exception.AuthenticationException
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.UserSecurity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val customerRepository: CustomerRepository
): UserDetailsService {
    override fun loadUserByUsername(id: String): UserDetails {
        val customer = customerRepository.findById(id.toInt()).orElseThrow{AuthenticationException("", "")}
        return UserSecurity(customer)
    }
}