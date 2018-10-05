package com.spring.bet_calculator.constraint

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CountryValidator : ConstraintValidator<Country, String>
{
    override fun isValid(p0: String, p1: ConstraintValidatorContext): Boolean
    {
        return CountryList.isValidCountry(p0)
    }

    override fun initialize(constraintAnnotation: Country)
    {}
}