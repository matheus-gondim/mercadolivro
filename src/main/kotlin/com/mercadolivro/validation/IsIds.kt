package com.mercadolivro.validation


import com.mercadolivro.validation.implementations.IsIdsValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [IsIdsValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class IsIds(
    val message: String = "Os ids da lista deve ser maiores que zero",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
