package info.deepidea.wordcounter.androidclient.acceptanceTest;

import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import info.deepidea.wordcounter.androidclient.MainActivity;

import static org.junit.Assert.assertTrue;

@Config(manifest = "./src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class RobolectricTest {
    @Test
    public void testSomething() throws Exception {
        Activity activity = Robolectric.buildActivity(MainActivity.class).create().get();
        assertTrue(activity != null);
    }
}