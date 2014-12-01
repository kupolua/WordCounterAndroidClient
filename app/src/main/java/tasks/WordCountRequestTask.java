package tasks;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qalight.javacourse.wordcounterandroidclient.MainActivity;
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

    private String requestText;

    private String sortingOrder = VALUE_DESCENDING;

    private String isFilterWords = FALSE;

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
    protected String doInBackground(T... params) {
        if (params == null || params.length < 1) {
            throw new IllegalArgumentException();
        }

        activity = params[0];

        try {
            return requestText == null || requestText.length()==0 ? "" : post(requestText, sortingOrder, isFilterWords);
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
                    Log.d(TAG, "Click sortBtnCount");
                    EditText inputView = (EditText) activity.findViewById(R.id.inputText);
                    WordCountRequestTask<MainActivity> wkrt = new WordCountRequestTask<MainActivity>();
                    wkrt.setRequestText(inputView.getEditableText().toString());
                    if (sortBtnWord.getText().equals("Word") || sortBtnWord.getText().equals("Word ↑")){
                        wkrt.setSortingOrder(KEY_DESCENDING);
                    } else {
                        wkrt.setSortingOrder(KEY_ASCENDING);
                    }
                    sortBtnCount.setText("Count");
                    wkrt.execute((MainActivity) activity);
                }
            });

            sortBtnCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Click sortBtnCount");
                    EditText inputView = (EditText) activity.findViewById(R.id.inputText);
                    WordCountRequestTask<MainActivity> wkrt = new WordCountRequestTask<MainActivity>();
                    wkrt.setRequestText(inputView.getEditableText().toString());
                    if (sortBtnCount.getText().equals("Count") || sortBtnCount.getText().equals("Count ↑")){
                        wkrt.setSortingOrder(VALUE_DESCENDING);
                    } else {
                        wkrt.setSortingOrder(VALUE_ASCENDING);
                    }
                    sortBtnWord.setText("Word");
                    wkrt.execute((MainActivity) activity);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Log.d(TAG, requestedSortingOrder);
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

}