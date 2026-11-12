package com.example.shop.logic

object ProductValidator {
    fun validateName(name: String?): Boolean =
        !name.isNullOrBlank() && name.length in 2..50

    fun validateQty(qty: Int): Boolean = qty in 1..99

    fun validatePrice(price: Double): Boolean = price > 0 && price <= 1_000_000
}
