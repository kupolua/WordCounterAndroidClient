package com.qalight.javacourse.wordcounterandroidclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

public class HomeFragment extends Fragment implements RequestInFragment, OnClickListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private ProgressBar spinner;
    private Map<String, Integer> countedResult;

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
    public void startExecute(WordCountRequestTask wkrt) {
        spinner.setVisibility(View.VISIBLE);
        hideError();
    }

    @Override
    public void finishExecute(WordCountRequestTask wkrt) {
        if (wkrt.hasError()) {
            showError(wkrt.getErrorResult());
        }

        if (wkrt.hasResult()) {
            countedResult = wkrt.getCountedResult();
            showResult(countedResult);
        }

        spinner.setVisibility(View.GONE);
    }

    private void sendRequest() {
        if (hasConnection(getActivity())) {
            CheckBox filter = (CheckBox) getActivity().findViewById(R.id.buttonFilter);
            EditText inputView = (EditText) getActivity().findViewById(R.id.inputText);

            WordCountRequestTask wkrt = new WordCountRequestTask(this);
            if (filter.isChecked())
                wkrt.setIsFilterWords(WordCountRequestTask.TRUE);
            wkrt.setRequestText(inputView.getEditableText().toString());
            wkrt.execute(this);
        }
    }

    private void showResult(Map<String, Integer> countResult) {
        TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.resultTable);
        tableLayout.removeAllViews();

        final TextView sortBtnWord = new TextView(getActivity());
        final TextView sortBtnCount = new TextView(getActivity());

        String wordBtnText = "Word";
        String countBtnText = "Count";
        sortBtnWord.setText(wordBtnText);
        sortBtnCount.setText(countBtnText);

        TableRow tableRow = new TableRow(getActivity());
        tableRow.addView(sortBtnWord);
        tableRow.addView(sortBtnCount);
        tableRow.setBackgroundColor(Color.parseColor("#cccccc"));
        tableRow.setPadding(5, 5, 5, 5);
        tableLayout.addView(tableRow);

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

    private void hideError() {
        ListView lvMain = (ListView) getActivity().findViewById(R.id.errorList);
        if (lvMain.getAdapter() != null && !lvMain.getAdapter().isEmpty()) {
            ((ArrayAdapter<String>) lvMain.getAdapter()).clear();
        }
    }

    private void showError(List<String> errorList) {
        ListView lvMain = (ListView) getActivity().findViewById(R.id.errorList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.error_list_item, errorList);
        lvMain.setAdapter(adapter);
    }

    private boolean hasConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }
}
