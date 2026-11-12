package com.example.shop

import com.example.shop.logic.ProductValidator
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductValidatorTest {
    @Test fun nameNull_invalid() = assertFalse(ProductValidator.validateName(null))
    @Test fun nameBlank_invalid() = assertFalse(ProductValidator.validateName("  "))
    @Test fun nameOneChar_invalid() = assertFalse(ProductValidator.validateName("a"))
    @Test fun nameTwoChars_valid() = assertTrue(ProductValidator.validateName("ab"))
    @Test fun qtyZero_invalid() = assertFalse(ProductValidator.validateQty(0))
    @Test fun qty99_valid() = assertTrue(ProductValidator.validateQty(99))
    @Test fun qty100_invalid() = assertFalse(ProductValidator.validateQty(100))
    @Test fun negativePrice_invalid() = assertFalse(ProductValidator.validatePrice(-1.0))
}
