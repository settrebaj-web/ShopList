package com.example.shop.data

import com.example.shop.util.EspressoIdling
import retrofit2.http.GET

data class RateResponse(val rate: Double)

/** Retrofit интерфейсі (желі қабаты, MockWebServer-мен тестіленеді). */
interface CurrencyService {
    @GET("rate")
    suspend fun rate(): RateResponse
}

/** Қолданба қабаты үшін интерфейс (тесттерде мок жасалады). */
interface CurrencyApi {
    suspend fun rate(): Double
}

class RetrofitCurrencyApi(private val service: CurrencyService) : CurrencyApi {
    override suspend fun rate(): Double =
        EspressoIdling.wrap { service.rate().rate }
}
