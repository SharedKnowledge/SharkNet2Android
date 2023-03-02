package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.contacts.receiveCredentials.ReceiveCredentialsFragment
import org.junit.Test

class ReceiveCredentialsTest {

    @Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<ReceiveCredentialsFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_receive_credentials)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_receive_credentials_abort_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_receive_credentials_certificate_description_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_receive_credentials_cic_explanation_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_receive_credentials_explanation_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_receive_credentials_save_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_receive_credentials_issue_certificate_description_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_receive_credentials_show_issued_certificates_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_receive_credentials_show_own_certificate_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}