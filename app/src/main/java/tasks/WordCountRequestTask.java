package tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.qalight.javacourse.wordcounterandroidclient.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import junit.framework.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 9/15/14.
 */
public class WordCountRequestTask<T extends Activity> extends AsyncTask<T, Void, String> {

	private T activity;
	private static final long SECOND = 1000;
	private final long DEFAULT_TIMEOUT = 30 * SECOND;
	private static final int PORT = 8008;
	private static final String PARAM_NAME = "textCount";
	private static final String SERVER_NAME = "http://95.158.60.148:";
	private static final String CONTEXT = "/WordCounter/";
	private static final String COUNT_REQUEST = "countWordsRestStyle";
	private static final String COUNT_URL = SERVER_NAME + PORT + CONTEXT + COUNT_REQUEST;
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private OkHttpClient client;

	@Override
	protected String doInBackground(T... params) {
		if (params == null || params.length < 1) {
			throw new IllegalArgumentException();
		}

		activity = params[0];

		try {
			EditText inputView = (EditText) activity.findViewById(R.id.inputText);
			String inputValue = inputView.getEditableText().toString();
			return inputValue == null || inputValue.length()==0 ? "" : post(COUNT_URL, inputValue);
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

	public Request buildRequestWithParamValue(String requestedValue) {
		RequestBody formBody = new FormEncodingBuilder()
				.add(PARAM_NAME, requestedValue)
				.build();
		final Request request = new Request.Builder()
				.url(COUNT_URL)
				.post(formBody)
				.build();
		return request;
	}

	private String createFailMessage(String requestedValue) {
		return "cannot get response from " + COUNT_URL + " with request: " + requestedValue;
	}

	private String post(String url, String textUrl) throws IOException {

		client = new OkHttpClient();

		final String requestedValue = textUrl;
		Request request = buildRequestWithParamValue(requestedValue);

		Response response = client.newCall(request).execute();

		WordCounterResponse wordCounterResponse=null;

		if (!response.isSuccessful()) {
			createFailMessage(requestedValue);
		} else {
			String resultStr = response.body().string();
			ObjectMapper objectMapper = new ObjectMapper();
			wordCounterResponse = objectMapper.readValue(resultStr, WordCounterResponse.class);

		}

		return wordCounterResponse.toString();
	}

	static final class WordCounterResponse{
		private final Map<String, Integer> countedResult;

		public WordCounterResponse() {
			countedResult = new HashMap<String, Integer>();
		}

		public WordCounterResponse(Map<String, Integer> countedResult) {
			this.countedResult = countedResult;
		}

		public Map<String, Integer> getCountedResult() {
			return countedResult;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			WordCounterResponse that = (WordCounterResponse) o;

			if (!countedResult.equals(that.countedResult)) return false;

			return true;
		}

		@Override
		public int hashCode() {
			return countedResult.hashCode();
		}

		/*@Override
		public String toString() {
			return "WordCounterResponse{" +
					"countedResult=" + countedResult +
					'}';
		}*/


		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, Integer> entry : countedResult.entrySet()) {
				sb.append(entry.getKey() + ", ");
				sb.append(entry.getValue() + "\n");
			}
			return sb.toString();
		}
	}

}