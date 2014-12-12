package com.qalight.javacourse.wordcounterandroidclient;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import junit.framework.Assert;

public class HomeFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    @TargetApi(Build.VERSION_CODES.FROYO)
    public HomeFragmentTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testOnCreateView() throws Exception {

    }

    public void testOnAttach() throws Exception {

    }

    public void testOnStart() throws Exception {

    }

    public void testOnClick() throws Exception {
        // given
        final String inputText = "one one two";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputText), inputText);
        solo.clickOnView(solo.getView(R.id.buttonOk));

        // then
        String expectedText = "one";
        Assert.assertTrue(solo.searchText(expectedText));
    }

    public void testStartExecute() throws Exception {

    }

    public void testFinishExecute() throws Exception {

    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}