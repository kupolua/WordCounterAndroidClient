package tasks;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.qalight.javacourse.wordcounterandroidclient.R;
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

    private static final String SERVER_NAME = "http://95.158.60.148:";
    private static final String CONTEXT = "/WordCounter/";
    private static final String COUNT_REQUEST = "countWordsWithParams";
    private static final String COUNT_URL = SERVER_NAME + PORT + CONTEXT + COUNT_REQUEST;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private Activity activity;
    private RequestInFragment fragment;
    private String requestText;

    private String sortingOrder = VALUE_DESCENDING;

    private String isFilterWords = FALSE;

    JSONObject countedResult;
    JSONArray errorResult;

    List<String> sysError = new ArrayList<String>();

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
        if (hasConnection(activity)) {
            try {
                if (requestText == null || requestText.length() == 0){
                    addSysError(activity.getString(R.string.error_no_text));
                } else {
                    return post(requestText, sortingOrder, isFilterWords);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        } else {
            addSysError(activity.getString(R.string.error_no_connection));
        }

        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        fragment.startExecute(this);
    }

    @Override
    protected void onPostExecute(String parsedTextResult) {
        JSONObject reader;

        try {
            reader = new JSONObject(parsedTextResult);

            errorResult = reader.getJSONArray("errors");
            countedResult = reader.getJSONObject("countedResult");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        fragment.finishExecute(this);
    }

    public boolean hasError() {
        return (!sysError.isEmpty() || (errorResult != null && errorResult.length() > 0));
    }

    public List<String> getErrorResult() {
        List<String> list = new ArrayList<String>();
        list.addAll(sysError);
        try {
            for (int i = 0; i < errorResult.length(); i++) {
                list.add(errorResult.getString(i));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
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
            Log.e(TAG, e.getMessage(), e);
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

    private String createFailMessage(String errorMsg) {
        return "{\"countedResult\":{},\"errors\":[\"" + errorMsg + "\"]}";
    }

    private String post(String requestedValue, String sortingResult, String filterWords)
            throws IOException {
        Log.d(TAG, "POST");
        String resultStr = null;
        OkHttpClient client = new OkHttpClient();
        String locale = Locale.getDefault().getLanguage();

        Request request = buildCountRequestWithAllParams(requestedValue, sortingResult, filterWords, locale);
        try {
            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                addSysError(activity.getString(R.string.request_can_not_be_executed));
            } else {
                resultStr = response.body().string();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            addSysError(activity.getString(R.string.the_service_is_temporarily_unavailable));
        }


        return resultStr;
    }

    private void addSysError(String errorMsg) {
        sysError.add(errorMsg);
    }

    private boolean hasConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}