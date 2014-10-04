package tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qalight.javacourse.wordcounterandroidclient.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
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
			final String url = "http://95.158.60.148:8008/WordCounter/countWords";
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

			MultiValueMap<String, String> wordCountMap = new LinkedMultiValueMap<String, String>();
//			wordCountMap.add("dataTypeResponse","json");
//			wordCountMap.add("dummyData","whatever");
			wordCountMap.add("userUrlsList","http://defas.com.ua/java/Policy_of_.UA.pdf");

//			ResponseEntity<WordCountValues> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, WordCountValues.class, wordCountMap);
			String result = restTemplate.getForObject(url, String.class, wordCountMap);
//			WordCountValues result = restTemplate.getForObject(url, WordCountValues.class, wordCountMap);*/


			return post(url, textUrl);
		} catch (Exception e) {
			Log.e("MainActivity", e.getMessage(), e);
		}

		return null;
	}

	@Override
	protected void onPostExecute(String parsedTextResult) {
		TextView resultText = (TextView) activity.findViewById(R.id.resultText);
		resultText.setText(parsedTextResult);
	}

	private String post(String url, String textUrl) throws IOException {
		/*OkHttpClient client = new OkHttpClient();
		final Gson gson = new Gson();
//		RequestBody body = RequestBody.create(JSON, json);

		RequestBody formBody = new FormEncodingBuilder()
				.add("dataTypeResponse","json")
				.add("dummyData","whatever")
				.add("userUrlsList", textUrl)
				.build();

		Request request = new Request.Builder()
				.url(url)
				.post(formBody)

				//just as an extra precaution
				.addHeader("Accept", "application/json, text/javascript,*; q=0.01")
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

		WordCountValues gist = gson.fromJson(response.body().charStream(), WordCountValues.class);
		if (gist.wordCountMap == null || gist.wordCountMap.size() < 1) return "";*/

		/*StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Integer> entry : gist.wordCountMap.entrySet()) {
			sb.append(entry.getKey() + ", ");
			sb.append(entry.getValue() + "\n");
		}*/

		StringBuilder sb = new StringBuilder();
		WordCountValues wordCountValues = new WordCountValues();
		wordCountValues.mockBean("вера", 340);
		wordCountValues.mockBean("надежда", 540);
		wordCountValues.mockBean("любовь", 840);
		wordCountValues.mockBean("сок", 340);
		wordCountValues.mockBean("фрукт", 540);
		wordCountValues.mockBean("пакет", 840);
		wordCountValues.mockBean("машина", 340);
		wordCountValues.mockBean("одежда", 540);
		wordCountValues.mockBean("носок", 840);
		wordCountValues.mockBean("тетрадь", 340);
		wordCountValues.mockBean("пульт", 540);
		wordCountValues.mockBean("мак", 840);
		wordCountValues.mockBean("пс", 340);
		wordCountValues.mockBean("лук", 540);
		wordCountValues.mockBean("муж", 840);
		wordCountValues.mockBean("жена", 340);
		wordCountValues.mockBean("свет", 540);
		wordCountValues.mockBean("страна", 840);
		wordCountValues.mockBean("воск", 340);
		wordCountValues.mockBean("блабла", 540);
		wordCountValues.mockBean("ойляля", 840);
		wordCountValues.mockBean("куку", 340);
		wordCountValues.mockBean("таксешно", 540);
		wordCountValues.mockBean("дела", 840);
		wordCountValues.mockBean("милость", 340);
		wordCountValues.mockBean("благодать", 540);
		wordCountValues.mockBean("отступ", 840);


		for (Map.Entry<String, Integer> entry : wordCountValues.getWordCountMap().entrySet()) {
			sb.append(entry.getKey() + ", ");
			sb.append(entry.getValue() + "\n");
		}

		return sb == null || sb.length()<1 ? "Bad Responce" : sb.toString();
	}

	class WordCountValues {

		Map<String, Integer> wordCountMap;

		public void mockBean(String key, Integer value) {
			if (wordCountMap == null) {
				wordCountMap = new HashMap<String, Integer>();
			}
			wordCountMap.put(key, value);
		}

		public Map<String, Integer> getWordCountMap() {
			return wordCountMap;
		}
	}

}