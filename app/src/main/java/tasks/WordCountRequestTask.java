package tasks;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.qalight.javacourse.wordcounterandroidclient.R;
import com.squareup.okhttp.FormEncodingBuilder;
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

import static utils.Constants.*;

public class WordCountRequestTask extends AsyncTask<RequestInFragment, Void, String> {

    private static final String TAG = WordCountRequestTask.class.getSimpleName();

    private Activity activity;
    private RequestInFragment fragment;
    private String requestText;
    private String sortingOrder = VALUE_DESCENDING;
    private String isFilterWords = FILTER_OFF;

    private JSONObject countedResult;
    private JSONArray errorResult;

    private List<String> errorsList = new ArrayList<String>();

    public WordCountRequestTask(RequestInFragment fragment) {
        this.fragment = fragment;
        activity = fragment.getActivity();
    }

    public void setRequestText(String text) {
        requestText = text;
    }

    public void setFilterWordsOn(String isFilterWords) {
        this.isFilterWords = isFilterWords;
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

            final String jsonArrayErrors = "errors";
            errorResult = reader.getJSONArray(jsonArrayErrors);
            final String jsonObjectCountedResult = "countedResult";
            countedResult = reader.getJSONObject(jsonObjectCountedResult);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        fragment.finishExecute(this);
    }

    public boolean hasError() {
        return (!errorsList.isEmpty() || (errorResult != null && errorResult.length() > 0));
    }

    public List<String> getErrorResult() {
        List<String> list = new ArrayList<String>();
        list.addAll(errorsList);
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
        Map<String, Integer> countedResultMap = new LinkedHashMap<String, Integer>();
        Iterator<?> keys = countedResult.keys();
        try {
            while (keys.hasNext()) {
                String key = (String) keys.next();
                int value = countedResult.getInt(key);
                countedResultMap.put(key, value);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return countedResultMap;
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

    private String post(String requestedValue, String sortingResult, String filterWords)
            throws IOException {
        Log.d(TAG, "POST");
        String resultStr = null;
        OkHttpClient client = new OkHttpClient();
        String locale = Locale.getDefault().getLanguage();

        Request request = buildCountRequestWithAllParams(requestedValue, sortingResult,
                                                         filterWords, locale);
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
        errorsList.add(errorMsg);
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