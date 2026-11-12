package com.example.shop.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.ShopApp
import com.example.shop.logic.ProductValidator
import com.example.shop.model.Product
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private val vm: OrderViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as ShopApp
                return OrderViewModel(app.repository, app.currencyApi) as T
            }
        }
    }

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etQty: EditText
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.i("MainActivity ашылды")

        etName = findViewById(R.id.etName)
        etPrice = findViewById(R.id.etPrice)
        etQty = findViewById(R.id.etQty)
        tvTotal = findViewById(R.id.tvTotal)

        val adapter = ProductAdapter { p ->
            startActivity(
                Intent(this, DetailActivity::class.java).putExtra(DetailActivity.EXTRA_ID, p.id)
            )
        }
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        // Свайп арқылы өшіру (TC-04)
        val swipe = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {
                vm.delete(adapter.currentList[vh.bindingAdapterPosition].id)
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(recycler)

        findViewById<Button>(R.id.btnAdd).setOnClickListener { onAddClicked() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { vm.items.collect { adapter.submitList(it) } }
                launch {
                    vm.total.collect { tvTotal.text = getString(R.string.total_fmt, it) }
                }
            }
        }
        vm.load()
    }

    private fun onAddClicked() {
        val name = etName.text.toString().trim()
        val price = etPrice.text.toString().toDoubleOrNull()
        val qty = etQty.text.toString().toIntOrNull()

        when {
            !ProductValidator.validateName(name) ->
                etName.error = getString(R.string.err_name)
            price == null || !ProductValidator.validatePrice(price) ->
                etPrice.error = getString(R.string.err_price)
            qty == null || !ProductValidator.validateQty(qty) ->
                etQty.error = getString(R.string.err_qty)
            else -> {
                vm.add(Product(name = name, price = price, qty = qty))
                etName.text.clear()
                etPrice.text.clear()
                etQty.text.clear()
            }
        }
    }
}