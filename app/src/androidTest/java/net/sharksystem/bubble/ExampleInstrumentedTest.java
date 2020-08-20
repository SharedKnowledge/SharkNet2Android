package net.sharksystem.bubble;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
//import android.support.test.runner.AndroidJUnit4;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import android.view.View;

import androidx.test.espresso.ViewAssertion;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import net.sharksystem.R;
import net.sharksystem.makan.android.MakanListActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static java.util.regex.Pattern.matches;
import static org.junit.Assert.*;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MakanListActivity> activityRule =
            new ActivityTestRule<>(MakanListActivity.class);

    @Test
    public void listGoesOverTheFold() {
        //onView(withText("Hello world!")).check(matches(isDisplayed()));
    }

}
