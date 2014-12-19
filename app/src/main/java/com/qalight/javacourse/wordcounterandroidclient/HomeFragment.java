package com.qalight.javacourse.wordcounterandroidclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.List;
import java.util.Map;

import tasks.RequestInFragment;
import tasks.WordCountRequestTask;
import utils.WordResultSorter;

import static utils.Constants.*;

public class HomeFragment extends Fragment implements RequestInFragment, OnClickListener {

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
        activity.setTitle(R.string.title_home);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        EditText editText = (EditText) getActivity().findViewById(R.id.inputTextArea);
        editText.setText(sharedText);

        Button buttonCountWords = (Button) getActivity().findViewById(R.id.buttonCountWords);
        buttonCountWords.setOnClickListener(this);

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
            Map<String, Integer> countingWordsMap = sortResultByDefaultSorting(requestTask);
            showCountingWords(countingWordsMap);
        }
        spinner.setVisibility(View.GONE);
    }

    private Map<String, Integer> sortResultByDefaultSorting(WordCountRequestTask requestTask) {
        Map<String, Integer> countedResult = requestTask.getCountedResult();

        WordResultSorter sorter = WordResultSorter.valueOf(sortingOrder);
        return sorter.getSortedWords(countedResult);
    }

    private void showError(List<String> errorList) {
        ListView errorListView = (ListView) getActivity().findViewById(R.id.errorList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.error_list_item, errorList);
        errorListView.setAdapter(adapter);
    }

    private void showCountingWords(Map<String, Integer> result) {

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
                showCountingWords(sortedResult);
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
                showCountingWords(sortedResult);
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
        EditText inputTextView = (EditText) getActivity().findViewById(R.id.inputTextArea);
        CheckBox filterCheckBox = (CheckBox) getActivity().findViewById(R.id.checkBoxFilter);

        WordCountRequestTask requestTask = new WordCountRequestTask(this);

        if (filterCheckBox.isChecked()) {
            requestTask.setFilterWordsOn(FILTER_ON);
        }
        requestTask.setRequestText(inputTextView.getEditableText().toString());
        requestTask.execute(this);
    }


    private void hideError() {
        ListView errorListView = (ListView) getActivity().findViewById(R.id.errorList);
        if (errorListView.getAdapter() != null && !errorListView.getAdapter().isEmpty()) {
            ((ArrayAdapter<String>) errorListView.getAdapter()).clear();
        }
    }
}
