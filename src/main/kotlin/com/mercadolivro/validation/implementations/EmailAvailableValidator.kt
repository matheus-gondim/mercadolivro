package com.mercadolivro.validation.implementations

import com.mercadolivro.service.CustomerService
import com.mercadolivro.validation.EmailAvailable
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EmailAvailableValidator(
    private val customerService: CustomerService
) : ConstraintValidator<EmailAvailable, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value.isNullOrEmpty()) return false
        return customerService.emailAvailable(value)
    }
}
