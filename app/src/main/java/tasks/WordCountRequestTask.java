package tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qalight.javacourse.wordcounterandroidclient.R;
import com.qalight.javacourse.wordcounterandroidclient.WordCountBean;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by apple on 9/15/14.
 */
public class WordCountRequestTask<T extends Activity> extends AsyncTask<T, Void, String> {

	private T activity;
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	@Override
	protected String doInBackground(T... params) {
		if (params == null || params.length < 1) {
			throw new IllegalArgumentException();
		}

		activity = params[0];

		try {
			final String url = "http://95.158.60.148:8008/WordCounter";
			final String textUrl = "http://www.eslfast.com/supereasy/se/supereasy002.htm";

			/*// Set the Content-Type header
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setContentType(new MediaType("application", "json"));
			HttpEntity<String> requestEntity = new HttpEntity<String>(url, requestHeaders);

			RestTemplate restTemplate = new RestTemplate();

			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());


			// Make the HTTP POST request, marshaling the request to JSON, and the response to a String

			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//			map.add("dataTypeResponse","json");
//			map.add("dummyData","whatever");
			map.add("userUrlsList","http://defas.com.ua/java/Policy_of_.UA.pdf");

//			ResponseEntity<WordCountBean> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, WordCountBean.class, map);
			String result = restTemplate.getForObject(url, String.class, map);
//			WordCountBean result = restTemplate.getForObject(url, WordCountBean.class, map);*/


			return post(url, "Input url", textUrl);
		} catch (Exception e) {
			Log.e("MainActivity", e.getMessage(), e);
		}

		return null;
	}

	@Override
	protected void onPostExecute(String dummyData) {
		TextView greetingContentText = (TextView) activity.findViewById(R.id.content_value);
		greetingContentText.setText(dummyData);
	}

	private String post(String url, String formVar, String textUrl) throws IOException {
		OkHttpClient client = new OkHttpClient();
		final Gson gson = new Gson();
//		RequestBody body = RequestBody.create(JSON, json);

		RequestBody formBody = new FormEncodingBuilder()
				.add(formVar, textUrl)
				.build();

		Request request = new Request.Builder()
				.url(url)
				.post(formBody)

				//just as an extra precaution
				.addHeader("Accept", "application/json, text/javascript, */*; q=0.01")
				.addHeader("Accept-Encoding","gzip, deflate")
				.addHeader("Accept-Language","en-US,en;q=0.5")
				.addHeader("Cache-Control",	"no-cache")
				.addHeader("Connection","keep-alive")
				.addHeader("Content-Length","96")
				.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
				.addHeader("Host","95.158.60.148:8008")
				.addHeader("Pragma","no-cache")
				.addHeader("Referer","http://95.158.60.148:8008/WordCounter/")
				.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:32.0) Gecko/20100101 Firefox/32.0")
				.addHeader("X-Requested-With","XMLHttpRequest")

				.build();
		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

		Gist gist = gson.fromJson(response.body().charStream(), Gist.class);
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, GistFile> entry : gist.files.entrySet()) {
			sb.append(entry.getKey() + ", ");
			sb.append(entry.getValue().content + "\n");
		}

		return sb == null || sb.length()<1 ? "Bad Responce" : sb.toString();
	}

	class Gist {
		Map<String, GistFile> files;
	}

	class GistFile {
		String content;
	}
}