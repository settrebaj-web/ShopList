package com.example.shop

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shop.pages.MainPage
import com.example.shop.ui.MainActivity
import com.example.shop.util.EspressoIdling
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddProductPageObjectTest {
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun register() {
        IdlingRegistry.getInstance().register(EspressoIdling.resource)
    }

    @After
    fun unregister() {
        IdlingRegistry.getInstance().unregister(EspressoIdling.resource)
    }

    @Test
    fun addProduct() {
        MainPage()
            .typeName("Сыр").typePrice("300").typeQty("1")
            .clickAdd()
            .assertItemShown("Сыр")
    }

    @Test
    fun emptyName_showsError() {
        MainPage().clickAdd().assertNameError("Введите название")
    }
}
