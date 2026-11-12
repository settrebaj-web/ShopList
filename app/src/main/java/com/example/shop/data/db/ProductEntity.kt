package com.example.shop.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shop.model.Product

@Entity(tableName = "Product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val price: Double,
    val qty: Int,
    @ColumnInfo(defaultValue = "''") val note: String = ""
) {
    fun toModel() = Product(id, name, price, qty)
}
