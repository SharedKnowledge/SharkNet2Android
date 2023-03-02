package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.settings.SettingsFragment
import org.junit.Test

class SettingsFragmentTest {

    @Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<SettingsFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_settings)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_settings_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}