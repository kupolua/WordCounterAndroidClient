package com.qalight.javacourse.wordcounterandroidclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import tasks.RequestInFragment;
import tasks.WordCountRequestTask;
import tasks.WordResultSorter;

public class HomeFragment extends Fragment implements RequestInFragment, OnClickListener {
    public static final String KEY_ASCENDING = "KEY_ASCENDING";
    public static final String KEY_DESCENDING = "KEY_DESCENDING";
    public static final String VALUE_ASCENDING = "VALUE_ASCENDING";
    public static final String VALUE_DESCENDING = "VALUE_DESCENDING";
    String sortingOrder = VALUE_DESCENDING;
    private ProgressBar spinner;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).setTitle(R.string.title_home);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        EditText editText = (EditText) getActivity().findViewById(R.id.inputText);
        editText.setText(sharedText);

        Button buttonOk = (Button) getActivity().findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(this);

        spinner = (ProgressBar) getActivity().findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        sendRequest();
    }

    @Override
    public void startExecute(WordCountRequestTask requestTask) {
        spinner.setVisibility(View.VISIBLE);
        hideError();
    }

    @Override
    public void finishExecute(WordCountRequestTask requestTask) {
        if (requestTask.hasError()) {
            showError(requestTask.getErrorResult());
        }
        if (requestTask.hasResult()) {
            Map<String, Integer> defaultResult = sortResultByDefaultSorting(requestTask);
            showResult(defaultResult);
        }
        spinner.setVisibility(View.GONE);
    }

    private Map<String, Integer> sortResultByDefaultSorting(WordCountRequestTask requestTask) {
        Map<String, Integer> countedResult = requestTask.getCountedResult();

        WordResultSorter sorter = WordResultSorter.valueOf(sortingOrder);
        return sorter.getSortedWords(countedResult);
    }

    private void showError(List<String> errorList) {
        ListView lvMain = (ListView) getActivity().findViewById(R.id.errorList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.error_list_item, errorList);
        lvMain.setAdapter(adapter);
    }

    private void showResult(Map<String, Integer> result) {

        TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.resultTable);
        tableLayout.removeAllViews();

        final TextView sortBtnWord = new TextView(getActivity());
        final TextView sortBtnCount = new TextView(getActivity());
        String wordBtnText = getResources().getString(R.string.table_head_word);
        String countBtnText = getResources().getString(R.string.table_head_count);

        if (sortingOrder.equals(KEY_DESCENDING)) {
            wordBtnText = getResources().getString(R.string.table_head_word_up);
        }
        if (sortingOrder.equals(KEY_ASCENDING)) {
            wordBtnText = getResources().getString(R.string.table_head_word_down);
        }
        if (sortingOrder.equals(VALUE_DESCENDING)) {
            countBtnText = getResources().getString(R.string.table_head_count_up);
        }
        if (sortingOrder.equals(VALUE_ASCENDING)) {
            countBtnText = getResources().getString(R.string.table_head_count_down);
        }

        sortBtnWordOnClickListener(sortBtnWord, result);
        sortBtnCountOnClickListener(sortBtnCount, result);

        sortBtnWord.setText(wordBtnText);
        sortBtnCount.setText(countBtnText);

        TableRow tableRow = new TableRow(getActivity());
        tableRow.addView(sortBtnWord);
        tableRow.addView(sortBtnCount);
        final String grayColor = "#cccccc";
        tableRow.setBackgroundColor(Color.parseColor(grayColor));
        tableRow.setPadding(5, 5, 5, 5);
        tableLayout.addView(tableRow);

        createTableBodyOnLayout(result, tableLayout);
    }

    private void sortBtnCountOnClickListener(TextView sortBtnCount, final Map<String, Integer> map) {
        sortBtnCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WordResultSorter sorter;
                if (sortingOrder.equals(VALUE_ASCENDING)) {
                    sorter = WordResultSorter.VALUE_DESCENDING;
                    sortingOrder = VALUE_DESCENDING;
                } else {
                    sorter = WordResultSorter.VALUE_ASCENDING;
                    sortingOrder = VALUE_ASCENDING;
                }

                Map<String, Integer> sortedResult = sorter.getSortedWords(map);
                showResult(sortedResult);
            }
        });
    }

    private void sortBtnWordOnClickListener(TextView sortBtnWord, final Map<String, Integer> map) {
        sortBtnWord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WordResultSorter sorter;
                if (sortingOrder.equals(KEY_ASCENDING)) {
                    sorter = WordResultSorter.KEY_DESCENDING;
                    sortingOrder = KEY_DESCENDING;
                } else {
                    sorter = WordResultSorter.KEY_ASCENDING;
                    sortingOrder = KEY_ASCENDING;
                }

                Map<String, Integer> sortedResult = sorter.getSortedWords(map);
                showResult(sortedResult);
            }
        });
    }

    private void createTableBodyOnLayout(Map<String, Integer> countResult, TableLayout tableLayout) {
        TableRow tableRow;
        for (Map.Entry<String, Integer> entry : countResult.entrySet()) {
            TextView txt1 = new TextView(getActivity());
            TextView txt2 = new TextView(getActivity());

            txt1.setText(entry.getKey());
            txt2.setText(String.valueOf(entry.getValue()));
            tableRow = new TableRow(getActivity());
            tableRow.addView(txt1);
            tableRow.addView(txt2);
            tableLayout.addView(tableRow);
        }
    }

    private void sendRequest() {
        if (hasConnection(getActivity())) {
            CheckBox filter = (CheckBox) getActivity().findViewById(R.id.buttonFilter);
            EditText inputView = (EditText) getActivity().findViewById(R.id.inputText);
            if (inputView.getEditableText().toString().length() > 0) {
                WordCountRequestTask requestTask = new WordCountRequestTask(this);
                if (filter.isChecked())
                    requestTask.setIsFilterWords(WordCountRequestTask.TRUE);
                requestTask.setRequestText(inputView.getEditableText().toString());
                requestTask.execute(this);
            } else {
                Toast.makeText(getActivity(), R.string.error_no_text, Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.error_no_connection, Toast.LENGTH_LONG).show();
        }
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

    private void hideError() {
        ListView lvMain = (ListView) getActivity().findViewById(R.id.errorList);
        if (lvMain.getAdapter() != null && !lvMain.getAdapter().isEmpty()) {
            ((ArrayAdapter<String>) lvMain.getAdapter()).clear();
        }
    }
}
