package com.qalight.javacourse.wordcounterandroidclient;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

import org.hamcrest.Matchers;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.typeText;
import static com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions.matches;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.hasSibling;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.isDescendantOfA;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.allOf;

public class EspressoTest extends ActivityInstrumentationTestCase2<MainActivity> {
    @TargetApi(Build.VERSION_CODES.FROYO)
    public EspressoTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testEspressoClickingListViewPopulatesTextView() {
        //Given the ListView is populated
        onView(withId(R.id.buttonOk)).perform(ViewActions.click());
    }

    public void testCountWords_header2() {
        String expectedText = "Получить статистику частоты употребления слов!";
//        String expectedText = "Get most used words!";

        onView(withId(R.id.aboutTitle)).check(matches(withText(expectedText)));
    }

    public void testCountWords_plainText() {
        String inputText = "the two one TWO";
        String expectedText = "the two one 2 1 1";

        onView(withId(R.id.inputText)).perform(typeText(inputText));
        onView(withId(R.id.buttonOk)).perform(click());
//        onView(allOf(
//                isDescendantOfA(result)));
//        onView(withId(R.id.resultTable)).check(matches(withText(expectedText)));
    }
}
