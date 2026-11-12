package com.example.shop

import com.example.shop.data.ProductRepository
import com.example.shop.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/** Fake: жадында жұмыс істейтін қарапайым нақты іске асыру (mock емес). */
class FakeProductRepository : ProductRepository {
    private val state = MutableStateFlow<List<Product>>(emptyList())

    override fun observeAll(): Flow<List<Product>> = state

    override suspend fun add(p: Product) {
        state.value = state.value + p.copy(id = state.value.size + 1L)
    }

    override suspend fun delete(id: Long) {
        state.value = state.value.filterNot { it.id == id }
    }
}
