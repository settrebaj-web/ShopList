package com.example.shop.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shop.data.CurrencyApi
import com.example.shop.data.ProductRepository
import com.example.shop.logic.PriceCalculator
import com.example.shop.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class OrderViewModel(
    private val repo: ProductRepository,
    private val api: CurrencyApi
) : ViewModel() {

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    private val _items = MutableStateFlow<List<Product>>(emptyList())
    val items: StateFlow<List<Product>> = _items.asStateFlow()

    fun load() = viewModelScope.launch {
        val rate = try {
            api.rate()
        } catch (e: Exception) {
            Timber.w(e, "Курс алынбады, 1.0 қолданылады")
            1.0
        }
        repo.observeAll().collect { list ->
            _items.value = list
            _total.value = PriceCalculator.total(list) * rate
            Timber.d("Тауар саны: %d, жиыны: %.2f", list.size, _total.value)
        }
    }

    fun add(product: Product) = viewModelScope.launch { repo.add(product) }

    fun delete(id: Long) = viewModelScope.launch { repo.delete(id) }
}
