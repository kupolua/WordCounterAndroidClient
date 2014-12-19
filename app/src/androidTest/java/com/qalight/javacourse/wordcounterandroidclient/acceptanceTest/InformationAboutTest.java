package com.qalight.javacourse.wordcounterandroidclient.acceptanceTest;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qalight.javacourse.wordcounterandroidclient.MainActivity;
import com.qalight.javacourse.wordcounterandroidclient.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;

public class InformationAboutTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    @TargetApi(Build.VERSION_CODES.FROYO)
    public InformationAboutTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testAboutUs() throws Exception {
        // given
        final int navigationDrawerNum = 1;
        final int aboutUsNum = 1;
        final String expectedResult = "O нас";

        // when
        solo.clickOnView(solo.getView(FrameLayout.class, navigationDrawerNum));
        solo.clickOnView(solo.getView(android.R.id.text1, aboutUsNum));

        String actualResult = ((TextView) solo.getView(R.id.textView)).getText().toString();

        // then
        Assert.assertEquals(expectedResult, actualResult);
    }
}
