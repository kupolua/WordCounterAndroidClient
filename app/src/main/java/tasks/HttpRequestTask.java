package tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.qalight.javacourse.wordcounterandroidclient.Greeting;
import com.qalight.javacourse.wordcounterandroidclient.R;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by apple on 9/15/14.
 */
public class HttpRequestTask<T extends Activity> extends AsyncTask<T, Void, Greeting> {

	private T activity;

	@Override
	protected Greeting doInBackground(T... params) {
		if (params == null || params.length < 1) {
			throw new IllegalArgumentException();
		}

		activity = params[0];

		try {
			final String url = "http://rest-service.guides.spring.io/greeting";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			Greeting greeting = restTemplate.getForObject(url, Greeting.class);
			return greeting;
		} catch (Exception e) {
			Log.e("MainActivity", e.getMessage(), e);
		}

		return null;
	}

	@Override
	protected void onPostExecute(Greeting greeting) {
//		TextView greetingIdText = (TextView) activity.findViewById(R.id.id_value);
//		TextView greetingContentText = (TextView) activity.findViewById(R.id.content_value);
//		greetingIdText.setText(greeting.getId());
//		greetingContentText.setText(greeting.getContent());
	}

}