package com.spring.bet_calculator.constraint

import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.constraints.NotNull
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@NotNull
@Constraint(validatedBy = [(CountryValidator::class)])
@Target(FIELD, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, VALUE_PARAMETER, ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Documented
annotation class Country constructor(
        val message: String = "Invalid country",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)