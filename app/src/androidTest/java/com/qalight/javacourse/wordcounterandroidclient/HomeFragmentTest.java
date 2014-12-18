package com.qalight.javacourse.wordcounterandroidclient;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.qalight.javacourse.wordcounterandroidclient.utils.Util.getTableResult;

public class HomeFragmentTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    private int waitTime = 2000;

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
        Thread.sleep(waitTime);
        Map<String, String> actualResult = getTableResult(solo);

        // then
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
                put("one", "2");
                put("two", "1");
            }};
        Assert.assertEquals(expectedResult, actualResult);
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