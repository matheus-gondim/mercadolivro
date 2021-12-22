package com.mercadolivro.repository

import com.mercadolivro.model.entity.Purchase
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseRepository : CrudRepository<Purchase, Int> {
}