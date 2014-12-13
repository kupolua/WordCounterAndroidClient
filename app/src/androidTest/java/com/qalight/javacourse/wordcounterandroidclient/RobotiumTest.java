package com.qalight.javacourse.wordcounterandroidclient;

import android.annotation.TargetApi;
        import android.os.Build;
        import android.test.ActivityInstrumentationTestCase2;

        import com.robotium.solo.Solo;

        import junit.framework.Assert;

public class RobotiumTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;

    @TargetApi(Build.VERSION_CODES.FROYO)
    public RobotiumTest() {
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
        Assert.assertTrue(solo.searchText("Фильтровать"));
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