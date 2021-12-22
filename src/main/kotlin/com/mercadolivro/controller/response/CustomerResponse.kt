package com.mercadolivro.controller.response

import com.mercadolivro.model.enum.CustomerStatus

data class CustomerResponse(
    var id: Int? = null,
    var name: String,
    var email: String,
    var status: CustomerStatus? = null
)