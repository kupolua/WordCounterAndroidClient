package com.qalight.javacourse.wordcounterandroidclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

import tasks.HttpRequestTask;
import tasks.WordCountRequestTask;


public class MainActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	    Button buttonOk = (Button) findViewById(R.id.buttonOk);
	    buttonOk.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    getResult();
		    }
	    });

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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			clearResult();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getResult() {
		new WordCountRequestTask<MainActivity>().execute(this);
	}

	private void clearResult() {
		TextView resultText = (TextView) findViewById(R.id.resultText);
		resultText.setText("");
	}

}
