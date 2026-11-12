package com.example.shop

import android.app.Application
import android.os.StrictMode
import com.example.shop.data.CurrencyApi
import com.example.shop.data.CurrencyService
import com.example.shop.data.ProductRepository
import com.example.shop.data.RetrofitCurrencyApi
import com.example.shop.data.RoomProductRepository
import com.example.shop.data.db.AppDatabase
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ShopApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.build(this) }
    val repository: ProductRepository by lazy { RoomProductRepository(database.productDao()) }
    val currencyApi: CurrencyApi by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
        val service = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyService::class.java)
        RetrofitCurrencyApi(service)
    }

    override fun onCreate() {
        super.onCreate()
        // 1-тапсырма: debug-та толық лог, release-те тек warning/error
        if (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            Timber.plant(Timber.DebugTree())
            enableStrictMode()
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    // 13-тапсырма
    private fun enableStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build()
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedClosableObjects().detectActivityLeaks()
                .penaltyLog().build()
        )
    }

    companion object {
        // Өз API мекенжайыңызбен ауыстырыңыз
        const val BASE_URL = "https://example.com/"
    }
}
