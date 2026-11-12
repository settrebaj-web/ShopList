package com.example.shop

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.shop.data.db.AppDatabase
import com.example.shop.data.db.MIGRATION_1_2
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {
    private val dbName = "migration-test.db"

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
        emptyList(),
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrate1To2_keepsDataAndAddsNote() {
        helper.createDatabase(dbName, 1).apply {
            execSQL("INSERT INTO Product (id, name, price, qty) VALUES (1, 'A', 10.0, 1)")
            close()
        }

        val db = helper.runMigrationsAndValidate(dbName, 2, true, MIGRATION_1_2)

        db.query("SELECT name, note FROM Product WHERE id = 1").use { c ->
            c.moveToFirst()
            assertEquals("A", c.getString(0))
            assertEquals("", c.getString(1))
        }
    }
}
