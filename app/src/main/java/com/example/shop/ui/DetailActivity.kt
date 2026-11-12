package com.example.shop.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.shop.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val id = intent.getLongExtra(EXTRA_ID, -1)
        findViewById<TextView>(R.id.tvDetail).text = getString(R.string.detail_fmt, id)
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}
