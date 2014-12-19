package com.qalight.javacourse.wordcounterandroidclient.acceptanceTest;

import android.annotation.TargetApi;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.qalight.javacourse.wordcounterandroidclient.MainActivity;
import com.qalight.javacourse.wordcounterandroidclient.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.qalight.javacourse.wordcounterandroidclient.acceptanceTest.utils.Util.getTableResult;

public class SortingTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;
    private int waitTime = 2000;

    private final String inputText = "zebra ZEBRA automotive звон автомат";
    private final String httpLink = "http://kupol.in.ua/wordcounter/testData/test_sorting1.docx";
    private final int wordField = 0;
    private final int countField = 1;

    @TargetApi(Build.VERSION_CODES.FROYO)
    public SortingTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testSortWordDescending() throws Exception {
        // given
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("звон", "1");
            put("автомат", "1");
            put("zebra", "2");
            put("automotive", "1");
        }};

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), inputText);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        clickOnHeadTable(wordField);
        Thread.sleep(waitTime);

        clickOnHeadTable(wordField);
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testSortWordAscending() throws Exception {
        // given
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("automotive", "1");
            put("zebra", "2");
            put("автомат", "1");
            put("звон", "1");
        }};

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), inputText);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        clickOnHeadTable(wordField);
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testSortQuantityDescending() throws Exception {
        // given
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("звон", "2");
            put("zebra", "2");
            put("automotive", "1");
        }};

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), httpLink);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        clickOnHeadTable(countField);
        Thread.sleep(waitTime);

        clickOnHeadTable(countField);
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testSortQuantityAscending() throws Exception {
        // given
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("automotive", "1");
            put("звон", "2");
            put("zebra", "2");
        }};

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), httpLink);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        clickOnHeadTable(countField);
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Assert.assertEquals(expectedResult, actualResult);
    }

    private void clickOnHeadTable(int headNum) {
        final int firstElementOfTable = 0;
        TableLayout tableLayout = (TableLayout) solo.getView(R.id.resultTable);
        TableRow tableRow = (TableRow) tableLayout.getChildAt(firstElementOfTable);
        solo.clickOnView(tableRow.getChildAt(headNum));
    }
}
