package com.example.shop

import com.example.shop.logic.PriceCalculator
import com.example.shop.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class PriceCalculatorParamTest(
    private val sum: Double,
    private val expected: Double
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data() = listOf(
            arrayOf(0.0, 0.0),
            arrayOf(999.99, 999.99), // 1000-ға дейінгі шекара
            arrayOf(1000.0, 900.0), // 1000 шекарасы
            arrayOf(4999.0, 4499.1),
            arrayOf(5000.0, 4000.0), // 5000 шекарасы
            arrayOf(10000.0, 8000.0)
        )
    }

    @Test
    fun discountApplied() {
        // Arrange
        val items = listOf(Product(1, "Товар", sum, 1))
        // Act
        val result = PriceCalculator.total(items)
        // Assert
        assertEquals(expected, result, 0.001)
    }
}
