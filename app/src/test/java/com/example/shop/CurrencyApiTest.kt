package com.example.shop

import org.junit.Assert.assertTrue
import com.example.shop.data.CurrencyService
import com.google.gson.JsonParseException
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class CurrencyApiTest {
    private lateinit var server: MockWebServer
    private lateinit var api: CurrencyService

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient.Builder().readTimeout(1, TimeUnit.SECONDS).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyService::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun success() = runBlocking {
        server.enqueue(MockResponse().setBody("""{"rate":450.5}"""))
        assertEquals(450.5, api.rate().rate, 0.001)
    }

    @Test
    fun http404() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404))
        try {
            api.rate()
            fail("HttpException күтілді")
        } catch (e: HttpException) {
            assertEquals(404, e.code())
        }
    }

    @Test
    fun http500() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(500))
        try {
            api.rate()
            fail("HttpException күтілді")
        } catch (e: HttpException) {
            assertEquals(500, e.code())
        }
    }

    @Test
    fun timeout() = runBlocking {
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))
        try {
            api.rate()
            fail("SocketTimeoutException күтілді")
        } catch (e: SocketTimeoutException) {
            // күтілген нәтиже
        }
    }

    @Test
    fun badJson() = runBlocking {
        server.enqueue(MockResponse().setBody("{not json"))
        try {
            api.rate()
            fail("Жарамсыз JSON қатесі күтілді")
        } catch (e: java.io.IOException) {
            // MalformedJsonException осы класты мұрагерлейді
            assertTrue(e is com.google.gson.stream.MalformedJsonException)
        } catch (e: JsonParseException) {
            // JsonSyntaxException болса да қабылданады
        }
    }
}
