package com.qalight.javacourse.wordcounterandroidclient.acceptanceTest;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.qalight.javacourse.wordcounterandroidclient.MainActivity;
import com.qalight.javacourse.wordcounterandroidclient.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.qalight.javacourse.wordcounterandroidclient.utils.Util.getTableResult;

public class FilteringWordsTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    private int waitTime = 2000;

    @TargetApi(Build.VERSION_CODES.FROYO)
    public FilteringWordsTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testFilterOn() throws Exception {
        // given
        final String httpLink = "http://kupol.in.ua/wordcounter/testData/page_latin.html";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputText), httpLink);
        solo.clickOnView(solo.getView(R.id.checkBoxFilter));
        solo.clickOnView(solo.getView(R.id.buttonOk));
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("test", "3");
            put("santa-monica", "1");
        }};
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testFilterOff() throws Exception {
        // given
        final String httpLink = "http://kupol.in.ua/wordcounter/testData/page_latin.html";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputText), httpLink);
        solo.clickOnView(solo.getView(R.id.checkBoxFilter));
        solo.clickOnView(solo.getView(R.id.buttonOk));
        Thread.sleep(waitTime);

        solo.clickOnView(solo.getView(R.id.checkBoxFilter));
        solo.clickOnView(solo.getView(R.id.buttonOk));
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("test", "3");
            put("a", "1");
            put("santa-monica", "1");
        }};
        Assert.assertEquals(expectedResult, actualResult);
    }
}
