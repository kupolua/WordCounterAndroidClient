package com.qalight.javacourse.wordcounterandroidclient.acceptanceTest;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.qalight.javacourse.wordcounterandroidclient.MainActivity;
import com.qalight.javacourse.wordcounterandroidclient.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.Locale;

public class InformationAboutTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    private final String localizationRu = "ru";

    @TargetApi(Build.VERSION_CODES.FROYO)
    public InformationAboutTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        changeLocale(localizationRu);
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

    public void changeLocale(String locale ){
        Resources resources = getActivity().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        config.locale = new Locale(locale);
        resources.updateConfiguration(config, displayMetrics);
        getActivity().getResources().updateConfiguration(config, displayMetrics);

        getInstrumentation().runOnMainSync(new Runnable() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            public void run() {
                getActivity().recreate();
            }
        });
    }
}
