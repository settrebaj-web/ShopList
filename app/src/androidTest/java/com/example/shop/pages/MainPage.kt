package com.example.shop.pages

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.shop.R

/** 10-тапсырма: Page Object. Тесттер UI детальдарын білмейді. */
class MainPage {
    fun typeName(text: String) = apply {
        onView(withId(R.id.etName)).perform(replaceText(text), closeSoftKeyboard())
    }

    fun typePrice(text: String) = apply {
        onView(withId(R.id.etPrice)).perform(replaceText(text), closeSoftKeyboard())
    }

    fun typeQty(text: String) = apply {
        onView(withId(R.id.etQty)).perform(replaceText(text), closeSoftKeyboard())
    }

    fun clickAdd() = apply { onView(withId(R.id.btnAdd)).perform(click()) }

    fun assertItemShown(name: String) = apply {
        onView(withText(name)).check(matches(isDisplayed()))
    }

    fun assertNameError(msg: String) = apply {
        onView(withId(R.id.etName)).check(matches(hasErrorText(msg)))
    }
}
