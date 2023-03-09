package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import net.sharksystem.ui.channels.AddChannelFragment
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddChannelTest {

    @Test fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<AddChannelFragment>()
        //Espresso.onView(withId(R.id.fragment_add_channel)).check(matches(isDisplayed()))
        //Espresso.onView(withId(R.id.fragment_add_channel_add_name_explanation)).check(matches(isDisplayed()))
        //Espresso.onView(withId(R.id.fragment_add_channel_add_name_input_field)).check(matches(isDisplayed()))
        //Espresso.onView(withId(R.id.fragment_add_channel_channel_uri_explanation)).check(matches(isDisplayed()))
        //Espresso.onView(withId(R.id.fragment_add_channel_channel_uri)).check(matches(isDisplayed()))
        //Espresso.onView(withId(R.id.fragment_add_channel_add_button)).check(matches(isDisplayed()))
        //Espresso.onView(withId(R.id.fragment_add_channel_exit_button)).check(matches(isDisplayed()))
    }
}