package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.radar.RadarFragment
import org.junit.Test

class RadarFragmentTest {

    @Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<RadarFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_radar)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_radar_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}