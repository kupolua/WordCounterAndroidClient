package tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.qalight.javacourse.wordcounterandroidclient.WsDefaultTestBean;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by apple on 9/15/14.
 */
public class HttpRequestTask<T extends Activity> extends AsyncTask<T, Void, WsDefaultTestBean> {

	private T activity;

	@Override
	protected WsDefaultTestBean doInBackground(T... params) {
		if (params == null || params.length < 1) {
			throw new IllegalArgumentException();
		}

		activity = params[0];

		try {
			final String url = "http://rest-service.guides.spring.io/greeting";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			WsDefaultTestBean greeting = restTemplate.getForObject(url, WsDefaultTestBean.class);
			return greeting;
		} catch (Exception e) {
			Log.e("MainActivity", e.getMessage(), e);
		}

		return null;
	}

	@Override
	protected void onPostExecute(WsDefaultTestBean greeting) {
//		TextView greetingIdText = (TextView) activity.findViewById(R.id.id_value);
//		TextView greetingContentText = (TextView) activity.findViewById(R.id.content_value);
//		greetingIdText.setText(greeting.getId());
//		greetingContentText.setText(greeting.getContent());
	}

}
