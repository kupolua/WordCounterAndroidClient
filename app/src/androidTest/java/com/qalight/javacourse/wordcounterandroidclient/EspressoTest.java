package com.qalight.javacourse.wordcounterandroidclient;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.TextView;

import com.google.android.apps.common.testing.ui.espresso.Espresso;
import com.google.android.apps.common.testing.ui.espresso.UiController;
import com.google.android.apps.common.testing.ui.espresso.ViewAction;
import com.google.android.apps.common.testing.ui.espresso.action.ViewActions;
import com.google.android.apps.common.testing.ui.espresso.assertion.ViewAssertions;
import com.google.android.apps.common.testing.ui.espresso.matcher.ViewMatchers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;


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
        Espresso.onView(ViewMatchers.withId(R.id.buttonOk)).perform(ViewActions.click());
    }

    public void testCountWords_header2() {
        String expectedText = "Получить статистику частоты употребления слов!";
//        String expectedText = "Get most used words!";

        Espresso.onView(ViewMatchers.withId(R.id.aboutTitle)).check(ViewAssertions.matches(ViewMatchers.withText(expectedText)));
    }

    public void testCountWords_plainText() {
        String inputText = "текст";

        Espresso.onView(ViewMatchers.withId(R.id.inputText)).perform(new SetTextAction(inputText));
        Espresso.onView(ViewMatchers.withId(R.id.buttonOk)).perform(ViewActions.click());

//        onView(allOf(
//                isDescendantOfA(result)));
//        onView(withId(R.id.resultTable)).check(matches(withText(expectedText)));
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
