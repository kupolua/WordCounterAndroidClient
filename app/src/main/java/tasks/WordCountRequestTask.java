package tasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.qalight.javacourse.wordcounterandroidclient.MainActivity;
import com.qalight.javacourse.wordcounterandroidclient.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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


    public void setRequestText(String val){
        requestText = val;
    }

    public void setSortingOrder(String val){
        sortingOrder = val;
    }

    public void setIsFilterWords(String val){
        isFilterWords = val;
    }

    @Override
    protected String doInBackground(RequestInFragment... params) {

        fragment = params[0];
        activity = fragment.getActivity();

        fragment.startExecute(this);

        try {
            return requestText == null || requestText.length()==0 ? "" : post(requestText, sortingOrder, isFilterWords);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String parsedTextResult) {
        super.onPostExecute(parsedTextResult);

        Log.d(TAG, parsedTextResult);

        JSONObject reader;
        try {
            reader = new JSONObject(parsedTextResult);

            errorResult = reader.getJSONArray("errors");
            countedResult = reader.getJSONObject("countedResult");
            /*
            Iterator<?> keys = countedResult.keys();

            TableLayout tableLayout = (TableLayout) activity.findViewById(R.id.resultTable);
            tableLayout.removeAllViews();
*/
            /*final ToggleButton filter = (ToggleButton) activity.findViewById(R.id.buttonFilter);

            filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    WordCountRequestTask wkrt = new WordCountRequestTask();
                    if (isChecked) {
                        wkrt.setIsFilterWords(TRUE);
                    } else {
                        wkrt.setIsFilterWords(FALSE);
                    }
                    //wkrt.execute((MainActivity) activity);
                }
            });*/
/*
            final TextView sortBtnWord = new TextView(activity);
            final TextView sortBtnCount = new TextView(activity);

            String wordBtnText = "Word";
            String countBtnText = "Count";
            if (sortingOrder.equals(VALUE_DESCENDING)) {
                countBtnText += " ↓";
            }

            if (sortingOrder.equals(VALUE_ASCENDING)) {
                countBtnText += " ↑";
            }

            if (sortingOrder.equals(KEY_DESCENDING)) {
                wordBtnText += " ↓";
            }

            if (sortingOrder.equals(KEY_ASCENDING)) {
                wordBtnText += " ↑";
            }

            sortBtnWord.setText(wordBtnText);
            sortBtnCount.setText(countBtnText);

            sortBtnWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText inputView = (EditText) activity.findViewById(R.id.inputText);
                    WordCountRequestTask wkrt = new WordCountRequestTask();
                    wkrt.setRequestText(inputView.getEditableText().toString());
                    if (sortBtnWord.getText().equals("Word") || sortBtnWord.getText().equals("Word ↑")){
                        wkrt.setSortingOrder(KEY_DESCENDING);
                    } else {
                        wkrt.setSortingOrder(KEY_ASCENDING);
                    }
                    sortBtnCount.setText("Count");
                    //wkrt.execute((MainActivity) activity);
                }
            });

            sortBtnCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Click sortBtnCount");
                    EditText inputView = (EditText) activity.findViewById(R.id.inputText);
                    WordCountRequestTask wkrt = new WordCountRequestTask();
                    wkrt.setRequestText(inputView.getEditableText().toString());
                    if (sortBtnCount.getText().equals("Count") || sortBtnCount.getText().equals("Count ↑")){
                        wkrt.setSortingOrder(VALUE_DESCENDING);
                    } else {
                        wkrt.setSortingOrder(VALUE_ASCENDING);
                    }
                    sortBtnWord.setText("Word");
                    //wkrt.execute((MainActivity) activity);
                }
            });

            TableRow tableRow = new TableRow(activity);
            tableRow.addView(sortBtnWord);
            tableRow.addView(sortBtnCount);
            tableRow.setBackgroundColor(Color.parseColor("#cccccc"));
            tableRow.setPadding(5, 5, 5, 5);
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


*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragment.finishExecute(this);
    }

    public boolean hasError(){
        return (errorResult.length() > 0);
    }

    public List<String> getErrorResult() {
        List<String> list = new ArrayList<String>();
        try {
            for(int i = 0; i < errorResult.length(); i++){
                list.add(errorResult.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public Map<String, Integer> getCountedResult() {
        Map<String, Integer> map = new LinkedHashMap<String, Integer>();
        Iterator<?> keys = countedResult.keys();
        try {
            while( keys.hasNext() ){
                String key = (String)keys.next();
                int value = countedResult.getInt(key);
                map.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    private void showError(JSONArray errorResult) throws JSONException {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < errorResult.length(); i++){
            list.add(errorResult.getString(i));
        }

        ListView lvMain = (ListView) activity.findViewById(R.id.errorList);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                R.layout.error_list_item, list);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);
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

        Request request = buildCountRequestWithAllParams(requestedValue, sortingResult, filterWords, LANGUAGE_DEFAULT_EN);

        Response response = client.newCall(request).execute();

        String resultStr = null;

        if (!response.isSuccessful()) {
            createFailMessage(requestedValue);
        } else {
            resultStr = response.body().string();
        }
        return resultStr;
    }

    public boolean hasConnection(Context context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

}