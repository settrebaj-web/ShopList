package com.example.shop

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shop.ui.DetailActivity
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [34])
class PrefsRobolectricTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun sharedPreferences_saveAndRead() {
        val prefs = context.getSharedPreferences("shop", Context.MODE_PRIVATE)
        prefs.edit().putString("currency", "KZT").commit()
        assertEquals("KZT", prefs.getString("currency", null))
    }

    @Test
    fun resources_appNameExists() {
        assertEquals("ShopList", context.getString(R.string.app_name))
    }

    @Test
    fun intent_carriesExtra() {
        val intent = Intent(context, DetailActivity::class.java)
            .putExtra(DetailActivity.EXTRA_ID, 7L)
        assertEquals(7L, intent.getLongExtra(DetailActivity.EXTRA_ID, -1))
    }
}
