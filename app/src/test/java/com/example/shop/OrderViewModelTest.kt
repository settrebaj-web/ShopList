package com.example.shop

import com.example.shop.data.CurrencyApi
import com.example.shop.data.ProductRepository
import com.example.shop.model.Product
import com.example.shop.ui.OrderViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OrderViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun load_multipliesTotalByRate() = runTest(dispatcher) {
        val repo = mockk<ProductRepository>()
        val api = mockk<CurrencyApi>()
        every { repo.observeAll() } returns flowOf(listOf(Product(1, "A", 100.0, 2)))
        coEvery { api.rate() } returns 2.0

        val vm = OrderViewModel(repo, api)
        vm.load()
        advanceUntilIdle()

        assertEquals(400.0, vm.total.value, 0.001)
        coVerify(exactly = 1) { api.rate() }
        verify { repo.observeAll() }
    }

    @Test
    fun load_apiFails_usesRateOne() = runTest(dispatcher) {
        val repo = mockk<ProductRepository>()
        val api = mockk<CurrencyApi>()
        every { repo.observeAll() } returns flowOf(listOf(Product(1, "A", 100.0, 2)))
        coEvery { api.rate() } throws java.io.IOException("no network")

        val vm = OrderViewModel(repo, api)
        vm.load()
        advanceUntilIdle()

        assertEquals(200.0, vm.total.value, 0.001)
    }

    @Test
    fun fakeRepository_addAndDelete() = runTest(dispatcher) {
        val repo = FakeProductRepository()
        val api = mockk<CurrencyApi>()
        coEvery { api.rate() } returns 1.0

        val vm = OrderViewModel(repo, api)
        vm.load()
        vm.add(Product(name = "Хлеб", price = 50.0, qty = 2))
        advanceUntilIdle()
        assertEquals(1, vm.items.value.size)
        assertEquals(100.0, vm.total.value, 0.001)

        vm.delete(vm.items.value[0].id)
        advanceUntilIdle()
        assertEquals(0, vm.items.value.size)
    }
}
