package tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WordCountRequestTask extends AsyncTask<RequestInFragment, Void, String> {

    private static final String TAG = WordCountRequestTask.class.getSimpleName();

    private static final long SECOND = 1000;
    private final long DEFAULT_TIMEOUT = 30 * SECOND;
    private static final int PORT = 8008;

    private static final String PARAM_TEXT_COUNT = "textCount";

    public static final String PARAM_SORTING_ORDER = "sortingOrder";
    public static final String KEY_ASCENDING = "KEY_ASCENDING";
    public static final String KEY_DESCENDING = "KEY_DESCENDING";
    public static final String VALUE_ASCENDING = "VALUE_ASCENDING";
    public static final String VALUE_DESCENDING = "VALUE_DESCENDING";

    public static final String PARAM_IS_FILTER_WORDS = "isFilterWords";
    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String PARAM_LANGUAGE = "Accept-Language";
    public static final String LANGUAGE_DEFAULT_EN = "en-EN,en;q=0.5";
    public static final String LANGUAGE_RU = "ru";
    public static final String LANGUAGE_UK = "uk";

    private static final String SERVER_NAME = "http://95.158.60.148:";
    private static final String CONTEXT = "/WordCounter/";
    private static final String COUNT_REQUEST = "countWordsWithParams";
    private static final String COUNT_URL = SERVER_NAME + PORT + CONTEXT + COUNT_REQUEST;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client;

    private Activity activity;
    private RequestInFragment fragment;
    private String requestText;

    private String sortingOrder = VALUE_DESCENDING;

    private String isFilterWords = FALSE;

    JSONObject countedResult = null;
    JSONArray errorResult = null;

    public WordCountRequestTask(RequestInFragment _fragment) {
        fragment = _fragment;
        activity = fragment.getActivity();
    }

    public void setRequestText(String val) {
        requestText = val;
    }

    public void setSortingOrder(String val) {
        sortingOrder = val;
    }

    public void setIsFilterWords(String val) {
        isFilterWords = val;
    }


    @Override
    protected String doInBackground(RequestInFragment... params) {
        try {
            return requestText == null || requestText.length() == 0 ? "" : post(requestText, sortingOrder, isFilterWords);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        fragment.startExecute(this);
    }

    @Override
    protected void onPostExecute(String parsedTextResult) {
        Log.d(TAG, parsedTextResult);

        JSONObject reader;
        try {
            reader = new JSONObject(parsedTextResult);

            errorResult = reader.getJSONArray("errors");
            countedResult = reader.getJSONObject("countedResult");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragment.finishExecute(this);
    }

    public boolean hasError() {
        if (errorResult != null)
            return (errorResult.length() > 0);

        return false;
    }

    public List<String> getErrorResult() {
        List<String> list = new ArrayList<String>();
        try {
            for (int i = 0; i < errorResult.length(); i++) {
                list.add(errorResult.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean hasResult() {
        if (countedResult != null)
            return (countedResult.length() > 0);

        return false;
    }

    public Map<String, Integer> getCountedResult() {
        Map<String, Integer> map = new LinkedHashMap<String, Integer>();
        Iterator<?> keys = countedResult.keys();
        try {
            while (keys.hasNext()) {
                String key = (String) keys.next();
                int value = countedResult.getInt(key);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public Request buildCountRequestWithAllParams(String requestedValue,
                                                  String requestedSortingOrder,
                                                  String requestedIsFilterWords,
                                                  String requestedLanguageParam) {
        RequestBody formBody = new FormEncodingBuilder()
                .add(PARAM_TEXT_COUNT, requestedValue)
                .add(PARAM_SORTING_ORDER, requestedSortingOrder)
                .add(PARAM_IS_FILTER_WORDS, requestedIsFilterWords)
                .build();

        final Request request = new Request.Builder()
                .header(PARAM_LANGUAGE, requestedLanguageParam)
                .url(COUNT_URL)
                .post(formBody)
                .build();

        return request;
    }

    private String createFailMessage(String requestedValue) {
        return "cannot get response from " + COUNT_URL + " with request: " + requestedValue;
    }

    private String post(String requestedValue, String sortingResult, String filterWords)
            throws IOException {

        client = new OkHttpClient();

        String locale = Locale.getDefault().getLanguage();

        Request request = buildCountRequestWithAllParams(requestedValue, sortingResult, filterWords, locale);
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