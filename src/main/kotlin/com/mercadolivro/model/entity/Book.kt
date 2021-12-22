package com.mercadolivro.model.entity

import com.mercadolivro.exception.BadRequestException
import com.mercadolivro.model.enum.BookStatus
import com.mercadolivro.model.enum.Errors
import java.math.BigDecimal
import javax.persistence.*

@Entity(name = "books")
data class Book(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column
    var name: String,

    @Column
    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: Customer? = null,
) {
    @Column
    @Enumerated(EnumType.STRING)
    var status: BookStatus? = null
        set(value) {
            if (field == BookStatus.DELETADO || field == BookStatus.CANCELADO)
                throw BadRequestException(message = Errors.ML102.message.format(field), errorCode = Errors.ML102.code)
            field = value
        }

    constructor(
        id: Int? = null,
        name: String,
        price: BigDecimal,
        customer: Customer? = null,
        status: BookStatus?
    ) : this(id, name, price, customer) {
        this.status = status
    }
}