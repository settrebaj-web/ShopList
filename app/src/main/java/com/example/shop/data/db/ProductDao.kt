package com.example.shop.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface ProductDao {
    @Query("SELECT * FROM Product ORDER BY id")
    fun getAll(): Flow<List<ProductEntity>>

    @Insert
    suspend fun insert(product: ProductEntity): Long

    @Query("DELETE FROM Product WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}
