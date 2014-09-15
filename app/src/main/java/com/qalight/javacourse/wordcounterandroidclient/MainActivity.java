package com.qalight.javacourse.wordcounterandroidclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

import tasks.HttpRequestTask;


public class MainActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	    /**
	     * Prevent the use of hardware Settings icon
	     */
	    try {
		    ViewConfiguration config = ViewConfiguration.get(this);
		    Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
		    if(menuKeyField != null) {
			    menuKeyField.setAccessible(true);
			    menuKeyField.setBoolean(config, false);
		    }
	    } catch (Exception ex) {
		    Log.d(TAG, ex.getLocalizedMessage());
	    }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		new HttpRequestTask<MainActivity>().execute(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			new HttpRequestTask<MainActivity>().execute(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
