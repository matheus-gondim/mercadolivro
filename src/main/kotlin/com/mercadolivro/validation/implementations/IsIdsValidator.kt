package com.mercadolivro.validation.implementations

import com.mercadolivro.validation.IsIds
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class IsIdsValidator : ConstraintValidator<IsIds, Set<Int>> {
    override fun isValid(value: Set<Int>?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return true
        return !value.none { it <= 0 }
    }
}