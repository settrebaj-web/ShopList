package com.example.shop.data

import com.example.shop.data.db.ProductDao
import com.example.shop.data.db.ProductEntity
import com.example.shop.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ProductRepository {
    fun observeAll(): Flow<List<Product>>
    suspend fun add(p: Product)
    suspend fun delete(id: Long)
}

class RoomProductRepository(private val dao: ProductDao) : ProductRepository {
    override fun observeAll(): Flow<List<Product>> =
        dao.getAll().map { list -> list.map { it.toModel() } }

    override suspend fun add(p: Product) {
        dao.insert(ProductEntity(name = p.name, price = p.price, qty = p.qty))
    }

    override suspend fun delete(id: Long) {
        dao.deleteById(id)
    }
}
