package tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qalight.javacourse.wordcounterandroidclient.R;
import com.qalight.javacourse.wordcounterandroidclient.WordResultSorter;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WordCountRequestTask<T extends Activity> extends AsyncTask<T, Void, String> {

    private T activity;
    private static final long SECOND = 1000;
    private final long DEFAULT_TIMEOUT = 30 * SECOND;
    //    private static final int PORT = 8080;
    private static final int PORT = 8008;
    private static final String PARAM_NAME = "textCount";
    //    private static final String SERVER_NAME = "http://localhost:";
    private static final String SERVER_NAME = "http://95.158.60.148:";
    private static final String CONTEXT = "/WordCounter/";
    private static final String COUNT_REQUEST = "countWordsRestStyle";
    private static final String COUNT_URL = SERVER_NAME + PORT + CONTEXT + COUNT_REQUEST;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String DEFAULT_SORTING_PARAM = "VALUE_DESCENDING";
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
            return inputValue == null || inputValue.length() == 0 ? "" : post(COUNT_URL, inputValue);
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
                .header("Accept-Language", "en-EN,en;q=0.5")
                .url(COUNT_URL)
                .post(formBody)
                .build();
        return request;
    }

    private String createFailMessage(String requestedValue) {
        return "cannot get response from " + COUNT_URL + " with request: " + requestedValue;
    }

    private String post(String url, String textUrl) throws IOException, JSONException {

        client = new OkHttpClient();

        final String requestedValue = textUrl;
        Request request = buildRequestWithParamValue(requestedValue);

        Response response = client.newCall(request).execute();

        String resultStr = response.body().string();

        JSONObject reader = new JSONObject(resultStr);
        JSONObject countedResult  = reader.getJSONObject("countedResult");
//        Log.d("countedResult= ", countedResult.toString());

        Map<String, Integer> map = new LinkedHashMap<String, Integer>();
        Iterator<?> keys = countedResult.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            int value = countedResult.getInt(key);
            map.put(key, value);
//            Log.d("key=" + key, "value=" + value);
        }

        WordResultSorter sorter = WordResultSorter.VALUE_DESCENDING;
        Map<String, Integer> sortedRefinedCountedWords = sorter.getSortedWords(map);

        return sortedRefinedCountedWords.toString();
//
//        if (!response.isSuccessful()) {
//            createFailMessage(requestedValue);
//        } else {
//            String resultStr = response.body().string();
//            ObjectMapper objectMapper = new ObjectMapper();
//            wordCounterResponse = objectMapper.readValue(resultStr, WordCounterResultContainerImpl.class);
//        }
//
//        return wordCounterResponse.toString();
    }

    static final class WordCounterResultContainerImpl {
        private Map<String, Integer> countedResult;
        private List<String> errors;
    }
}