package com.example.shop.util

import androidx.test.espresso.idling.CountingIdlingResource

/** 9-тапсырма: Espresso асинхронды жұмысты күтуі үшін IdlingResource. */
object EspressoIdling {
    val resource = CountingIdlingResource("network")

    suspend fun <T> wrap(block: suspend () -> T): T {
        resource.increment()
        try {
            return block()
        } finally {
            resource.decrement()
        }
    }
}
