package info.deepidea.wordcounter.androidclient.unitTest;

import android.app.Activity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import info.deepidea.wordcounter.androidclient.MainActivity;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class RobolectricTest {

    @Test
    public void testActivity() throws Exception {
        Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();

        Assert.assertTrue(activity != null);
        //test line2
    }
}