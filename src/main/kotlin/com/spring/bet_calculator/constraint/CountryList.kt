package com.spring.bet_calculator.constraint

import com.google.common.io.Resources
import java.nio.charset.Charset

class CountryList
{
    companion object
    {
        private val countries: List<String>

        init
        {
            val url = Resources.getResource(CountryList::class.java, "/country/country_list.txt")
            countries = Resources.readLines(url, Charset.forName("UTF-8"))
        }

        fun isValidCountry(country: String) : Boolean
        {
            return countries.any { it.equals(country, ignoreCase = true) }
        }
    }
}