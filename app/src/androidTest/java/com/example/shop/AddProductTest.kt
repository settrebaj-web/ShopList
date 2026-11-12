package com.example.shop

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shop.ui.DetailActivity
import com.example.shop.ui.MainActivity
import com.example.shop.util.EspressoIdling
import org.hamcrest.Matchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddProductTest {
    @get:Rule
    val rule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun register() {
        IdlingRegistry.getInstance().register(EspressoIdling.resource)
        Intents.init()
    }

    @After
    fun unregister() {
        IdlingRegistry.getInstance().unregister(EspressoIdling.resource)
        Intents.release()
    }

    private fun addProduct(name: String, price: String, qty: String) {
        // replaceText пернетақтаға тәуелсіз, кириллицамен де жұмыс істейді
        onView(withId(R.id.etName)).perform(replaceText(name), closeSoftKeyboard())
        onView(withId(R.id.etPrice)).perform(replaceText(price), closeSoftKeyboard())
        onView(withId(R.id.etQty)).perform(replaceText(qty), closeSoftKeyboard())
        onView(withId(R.id.btnAdd)).perform(click())
    }

    /** Room Flow тізімді кешіктіріп жаңартады, сондықтан көрінгенше күтеміз. */
    private fun waitUntilShown(text: String, timeoutMs: Long = 5000) {
        val end = System.currentTimeMillis() + timeoutMs
        var last: Throwable? = null
        while (System.currentTimeMillis() < end) {
            try {
                onView(withText(text)).check(matches(isDisplayed()))
                return
            } catch (e: Throwable) {
                last = e
                Thread.sleep(100)
            }
        }
        throw last ?: AssertionError("Көрінбеді: $text")
    }

    @Test
    fun addProduct_showsInList_andTotal() {
        addProduct("Молоко", "100", "3")
        waitUntilShown("Молоко")
        onView(withId(R.id.tvTotal)).check(matches(withText(containsString("₽"))))
    }

    @Test
    fun clickItem_opensDetails() {
        addProduct("Чай", "200", "1")
        waitUntilShown("Чай")
        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
        intended(hasComponent(DetailActivity::class.java.name))
    }

    @Test
    fun emptyName_showsError() {
        onView(withId(R.id.btnAdd)).perform(click())
        onView(withId(R.id.etName)).check(matches(hasErrorText("Введите название")))
    }
}