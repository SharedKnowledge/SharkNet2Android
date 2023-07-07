package net.sharksystem.sharknet.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

/**
 * This class allows to check the item count
 * inside of a RecyclerView.
 * <p></p>
 * Use as following: onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(5));
 * <p></p>
 * Class is copied from here: <a href="https://stackoverflow.com/questions/36399787/how-to-count-recyclerview-items-with-espresso">Stack Overflow</a>
 */
public class RecyclerViewItemCountAssertion implements ViewAssertion {
    private final int expectedCount;

    public RecyclerViewItemCountAssertion(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), is(expectedCount));
    }
}
