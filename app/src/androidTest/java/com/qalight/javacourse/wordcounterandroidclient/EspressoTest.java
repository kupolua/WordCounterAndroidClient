package com.qalight.javacourse.wordcounterandroidclient;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.google.android.apps.common.testing.ui.espresso.UiController;
import com.google.android.apps.common.testing.ui.espresso.ViewAction;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static com.google.android.apps.common.testing.ui.espresso.Espresso.onView;
import static com.google.android.apps.common.testing.ui.espresso.action.ViewActions.click;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withId;
import static com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers.withText;

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
        onView(ViewMatchers.withId(R.id.buttonOk)).perform(click());
    }

    public void testCountWords_mainHeader() {
        // todo: Add tests for all locales.
        String expectedText = "Получить статистику частоты употребления слов!";
//        String expectedText = "Get most used words!";

        onView(ViewMatchers.withId(R.id.aboutTitle)).check(ViewAssertions.matches(ViewMatchers.withText(expectedText)));
    }

    public void testCountWords_plainText() {
        String inputText = "текст ыъЪэЭ Two one tWo";

        onView(withId(R.id.inputText)).perform(new SetTextAction(inputText));
        onView(withText(R.string.checkbox_filter)).perform(click());
        onView(withId(R.id.buttonOk)).perform(click());
        onView(withText(R.string.table_head_word)).perform(click());
    }

    private static class SetTextAction implements ViewAction {

        private CharSequence mTextToSet;

        public SetTextAction(CharSequence textToSet) {
            mTextToSet = textToSet;
        }

        @Override
        public Matcher<View> getConstraints() {
            return Matchers.allOf(ViewMatchers.withEffectiveVisibility(
                            ViewMatchers.Visibility.VISIBLE),
                    ViewMatchers.isAssignableFrom(TextView.class)
            );
        }

        @Override
        public String getDescription() {
            return "set text";
        }

        @Override
        public void perform(UiController uiController, View view) {
            ((TextView) view).setText(mTextToSet);
        }
    }
}
