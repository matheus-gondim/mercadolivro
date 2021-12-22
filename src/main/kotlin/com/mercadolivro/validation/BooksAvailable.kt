package com.mercadolivro.validation

import com.mercadolivro.validation.implementations.BooksAvailableValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [BooksAvailableValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BooksAvailable(
    val message: String = "Os livros devem ser livros ativos",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
