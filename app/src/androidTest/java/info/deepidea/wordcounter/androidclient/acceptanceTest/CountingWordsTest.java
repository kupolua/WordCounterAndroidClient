package info.deepidea.wordcounter.androidclient.acceptanceTest;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import info.deepidea.wordcounter.androidclient.MainActivity;
import info.deepidea.wordcounter.androidclient.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static info.deepidea.wordcounter.androidclient.acceptanceTest.utils.Util.getTableResult;

public class CountingWordsTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private final String localizationRu = "ru";
    private final int errorMassageNumOne = 0;
    private final int errorMassageNumTwo = 1;
    private Solo solo;
    private int waitTime = 3000;

    @TargetApi(Build.VERSION_CODES.FROYO)
    public CountingWordsTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testPlainText() throws Exception {
        // given
        final String text = "the аэросъемка, АЭРОСЪЕМКА, дЫмаРь, дымарь, Під'їзд, ґедзь";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), text);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("дымарь", "2");
            put("аэросъемка", "2");
            put("the", "1");
            put("ґедзь", "1");
            put("під'їзд", "1");
        }};
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testImproperInputEmail() throws Exception {
        // given
        changeLocale(localizationRu);
        Thread.sleep(waitTime);

        final String text = "kris@gmail.com";
        final String expectedResult = "Система не может обработать введенный текст. Пожалуйста," +
                " проверьте, не забыли ли Вы добавить 'http://' префикс к ссылке или ввели" +
                " нечитаемый текст.";
        final int errorMassageNum = 0;

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), text);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        ListView listView = (ListView) solo.getView(R.id.errorList);
        String actualResult = ((TextView) listView.getChildAt(errorMassageNum)).getText().toString();

        // then
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testEmptyInput() throws Exception {
        // given
        changeLocale(localizationRu);
        Thread.sleep(waitTime);

        final String text = " ";
        final String textInToast = "Заполните поле для текста";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), text);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));

        boolean result = solo.waitForText(textInToast);

        // then
        Assert.assertTrue(result);
    }

    public void testCountWordInWebPageViaUrl() throws Exception {
        // given
        final String httpLink = "http://kupol.in.ua/wordcounter/testData/page_cyrillic.html";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), httpLink);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("дымарь", "2");
            put("аэросъемка", "2");
            put("нет", "1");
            put("он", "1");
            put("щеголь", "1");
        }};
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testCountWordInDocumentViaUrl() throws Exception {
        // given
        final String httpLink = "http://kupol.in.ua/wordcounter/" +
                "testData/%D0%BA%D0%B8%D1%80%D0%B8%D0%BB%D0%BB%D0%B8%D1%86%D0%B0.pptx";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), httpLink);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("думи", "2");
            put("мої", "1");
        }};
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testEnterThreeLinks() throws Exception {
        // given
        final String httpLinks = "http://kupol.in.ua/wordcounter/testData/page_latin.html " +
                "http://kupol.in.ua/wordcounter/" +
                "testData/%D0%BA%D0%B8%D1%80%D0%B8%D0%BB%D0%BB%D0%B8%D1%86%D0%B0.pptx " +
                "http://kupol.in.ua/wordcounter/testData/letters%2Bnumbers.txt";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), httpLinks);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        // then
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("test", "3");
            put("думи", "2");
            put("nice", "1");
            put("santa-monica", "1");
            put("мої", "1");
            put("people", "1");
            put("a", "1");
        }};
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testEnterThreeLinks_withBrokenHtmlLink() throws Exception {
        // given
        changeLocale(localizationRu);
        Thread.sleep(waitTime);

        final String httpLinks = "http://kupol....in.ua/wordcounter/testData/page_latin.html " +
                "http://kupol.in.ua/wordcounter/" +
                "testData/%D0%BA%D0%B8%D1%80%D0%B8%D0%BB%D0%BB%D0%B8%D1%86%D0%B0.pptx " +
                "http://kupol.in.ua/wordcounter/testData/letters%2Bnumbers.txt";
        final String expectedErrorMassage = "Не удается подключится к удаленному серверу по " +
                "ссылке: >http://kupol....in.ua/wordcounter/testData/page_latin.html";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), httpLinks);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        ListView listView = (ListView) solo.getView(R.id.errorList);
        String actualErrorMassage = ((TextView) listView.getChildAt(errorMassageNumOne)).getText().toString();

        // then
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("думи", "2");
            put("nice", "1");
            put("people", "1");
            put("мої", "1");
        }};

        Assert.assertEquals(expectedResult, actualResult);
        Assert.assertEquals(expectedErrorMassage, actualErrorMassage);
    }

    public void testEnterThreeLinks_withTwoBrokenLinks() throws Exception {
        // given
        changeLocale(localizationRu);
        Thread.sleep(waitTime);

        final String httpLinks = "http://kupol....in.ua/wordcounter/testData/page_latin.html " +
                "http://kupol....in.ua/wordcounter/" +
                "testData/%D0%BA%D0%B8%D1%80%D0%B8%D0%BB%D0%BB%D0%B8%D1%86%D0%B0.pptx " +
                "http://kupol.in.ua/wordcounter/testData/letters%2Bnumbers.txt";
        final String expectedErrorMassageOne = "Не удается подключится к удаленному серверу по " +
                "ссылке: >http://kupol....in.ua/wordcounter/testData/page_latin.html";
        final String expectedErrorMassageTwo = "Не удается подключится к удаленному серверу по" +
                " ссылке: >http://kupol....in.ua/wordcounter/" +
                "testData/%D0%BA%D0%B8%D1%80%D0%B8%D0%BB%D0%BB%D0%B8%D1%86%D0%B0.pptx";

        // when
        solo.enterText((EditText) solo.getView(R.id.inputTextArea), httpLinks);
        solo.clickOnView(solo.getView(R.id.buttonCountWords));
        Thread.sleep(waitTime);

        Map<String, String> actualResult = getTableResult(solo);

        ListView listView = (ListView) solo.getView(R.id.errorList);
        String actualErrorMassageOne = ((TextView) listView.getChildAt(errorMassageNumOne)).
                getText().toString();

        String actualErrorMassageTwo = ((TextView) listView.getChildAt(errorMassageNumTwo)).
                getText().toString();

        // then
        Map<String, String> expectedResult = new LinkedHashMap<String, String>() {{
            put("nice", "1");
            put("people", "1");
        }};

        Assert.assertEquals(expectedResult, actualResult);
        Assert.assertEquals(expectedErrorMassageOne, actualErrorMassageOne);
        Assert.assertEquals(expectedErrorMassageTwo, actualErrorMassageTwo);
    }

    public void changeLocale(String locale) {
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
