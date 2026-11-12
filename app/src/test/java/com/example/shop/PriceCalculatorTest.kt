package com.example.shop

import com.example.shop.logic.PriceCalculator
import com.example.shop.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class PriceCalculatorTest {

    @Test
    fun emptyList_returnsZero() {
        assertEquals(0.0, PriceCalculator.total(emptyList()), 0.0)
    }

    @Test
    fun quantityMultipliesPrice() {
        val items = listOf(Product(1, "A", 100.0, 3))
        assertEquals(300.0, PriceCalculator.total(items), 0.0)
    }

    @Test
    fun severalItemsAreSummed() {
        val items = listOf(Product(1, "A", 600.0, 1), Product(2, "B", 600.0, 1))
        assertEquals(1080.0, PriceCalculator.total(items), 0.001)
    }
}
