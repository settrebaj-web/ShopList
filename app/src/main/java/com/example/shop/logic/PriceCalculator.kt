package com.example.shop.logic

import com.example.shop.model.Product

object PriceCalculator {
    /** Жеңілдік: 1000-нан бастап 10%, 5000-нан бастап 20%. */
    fun total(items: List<Product>): Double {
        val sum = items.sumOf { it.price * it.qty }
        val discount = when {
            sum >= 5000 -> 0.20
            sum >= 1000 -> 0.10// 2-тапсырма: қатені осында ">" деп енгізіп, отладчикпен табыңыз
            else -> 0.0
        }
        return sum * (1 - discount)
    }
}
