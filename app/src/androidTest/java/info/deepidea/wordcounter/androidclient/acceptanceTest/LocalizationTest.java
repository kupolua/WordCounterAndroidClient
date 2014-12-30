package info.deepidea.wordcounter.androidclient.acceptanceTest;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.util.DisplayMetrics;
import android.widget.TextView;

import info.deepidea.wordcounter.androidclient.MainActivity;
import info.deepidea.wordcounter.androidclient.R;
import com.robotium.solo.Solo;

import junit.framework.Assert;

import java.util.Locale;

public class LocalizationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;
    private int waitTime = 1000;

    @TargetApi(Build.VERSION_CODES.FROYO)
    public LocalizationTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testLocal_en() throws Exception {
        // given
        final String localizationEn = "en";
        final String expectedResult = "Get most used words!";

        // when
        changeLocale(localizationEn);
        Thread.sleep(waitTime);

        String actualResult = ((TextView) solo.getView(R.id.aboutUsContent)).getText().toString();

        // then
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testLocal_ru() throws Exception {
        // given
        final String localizationRu = "ru";
        final String expectedResult = "Получить статистику частоты употребления слов!";

        // when
        changeLocale(localizationRu);
        Thread.sleep(waitTime);

        String actualResult = ((TextView) solo.getView(R.id.aboutUsContent)).getText().toString();

        // then
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testLocal_uk() throws Exception {
        // given
        final String localizationUk = "uk";
        final String expectedResult = "Отримати статистику частоти вживання слів!";

        // when
        changeLocale(localizationUk);
        Thread.sleep(waitTime);

        String actualResult = ((TextView) solo.getView(R.id.aboutUsContent)).getText().toString();

        // then
        Assert.assertEquals(expectedResult, actualResult);
    }

    public void testLocal_fr() throws Exception {
        // given
        final String localizationFr = "fr";
        final String expectedResult = "Get most used words!";

        // when
        changeLocale(localizationFr);
        Thread.sleep(waitTime);

        String actualResult = ((TextView) solo.getView(R.id.aboutUsContent)).getText().toString();

        // then
        Assert.assertEquals(expectedResult, actualResult);
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
