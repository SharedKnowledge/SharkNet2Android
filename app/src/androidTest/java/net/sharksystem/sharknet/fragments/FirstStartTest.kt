package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.firstLaunch.FirstStartFragment
import org.junit.Test

class FirstStartTest {

    @Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<FirstStartFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_first_start)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_first_start_enter_name_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_first_start_name_input)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_first_start_save_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_first_start_hint)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}