package tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qalight.javacourse.wordcounterandroidclient.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Iterator;

public class WordCountRequestTask<T extends Activity> extends AsyncTask<T, Void, String> {

    private static final String TAG = WordCountRequestTask.class.getSimpleName();
	private T activity;
	private static final long SECOND = 1000;
	private final long DEFAULT_TIMEOUT = 30 * SECOND;
	private static final int PORT = 8008;
	private static final String PARAM_NAME = "textCount";
	private static final String SERVER_NAME = "http://95.158.60.148:";
	private static final String CONTEXT = "/WordCounter/";
	private static final String COUNT_REQUEST = "countWords";
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
        JSONObject reader;
        JSONObject countedResult = null;
        try {
            reader = new JSONObject(parsedTextResult);
            countedResult = reader.getJSONObject("countedResult");
            Iterator<?> keys = countedResult.keys();

            TableLayout tableLayout = (TableLayout) activity.findViewById(R.id.resultTable);
            tableLayout.removeAllViews();

            Button sortBtnWord = new Button(activity);
            Button sortBtnCount = new Button(activity);

            sortBtnWord.setText("Word");
            sortBtnCount.setText("Count");

            TableRow tableRow = new TableRow(activity);
            tableRow.addView(sortBtnWord);
            tableRow.addView(sortBtnCount);
            tableLayout.addView(tableRow);

            while( keys.hasNext() ){
                String key = (String)keys.next();

                TextView txt1 = new TextView(activity);
                TextView txt2 = new TextView(activity);

                txt1.setText(key);
                txt2.setText(countedResult.getString(key));
                tableRow = new TableRow(activity);
                tableRow.addView(txt1);
                tableRow.addView(txt2);
                tableLayout.addView(tableRow);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

        String resultStr = null;

		if (!response.isSuccessful()) {
			createFailMessage(requestedValue);
		} else {
			resultStr = response.body().string();
		}
		return resultStr;
	}


}