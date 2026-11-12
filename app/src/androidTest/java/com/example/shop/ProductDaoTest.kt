package com.example.shop

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shop.data.db.AppDatabase
import com.example.shop.data.db.ProductDao
import com.example.shop.data.db.ProductEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: ProductDao

    @Before
    fun create() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.productDao()
    }

    @After
    fun close() = db.close()

    @Test
    fun insertAndRead() = runBlocking {
        dao.insert(ProductEntity(name = "Хлеб", price = 50.0, qty = 2))
        val all = dao.getAll().first()
        assertEquals(1, all.size)
        assertEquals("Хлеб", all[0].name)
    }

    @Test
    fun deleteById_removesRow() = runBlocking {
        val id = dao.insert(ProductEntity(name = "Сыр", price = 300.0, qty = 1))
        dao.deleteById(id)
        assertEquals(0, dao.getAll().first().size)
    }
}
