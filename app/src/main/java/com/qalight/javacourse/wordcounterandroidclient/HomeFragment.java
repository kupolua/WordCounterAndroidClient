package com.qalight.javacourse.wordcounterandroidclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;
import java.util.Map;

import tasks.RequestInFragment;
import tasks.WordCountRequestTask;

public class HomeFragment  extends Fragment implements RequestInFragment {

    private static final String TAG = HomeFragment.class.getSimpleName();



    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onStart(){
        super.onStart();

        Intent intent = getActivity().getIntent();
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        EditText editText = (EditText) getActivity().findViewById(R.id.inputText);
        editText.setText(sharedText);

        Button buttonOk = (Button) getActivity().findViewById(R.id.buttonOk);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click buttonOk");
                sendRequest();
            }
        });

        ToggleButton filter = (ToggleButton) getActivity().findViewById(R.id.buttonFilter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click filter");
                sendRequest();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).setTitle(R.string.title_home);
    }

    @Override
    public void startExecute(WordCountRequestTask wkrt) {
        hideError();
    }

    @Override
    public void finishExecute(WordCountRequestTask wkrt) {
        if (wkrt.hasError()){
            showError(wkrt.getErrorResult());
        }

        showResult(wkrt.getCountedResult());
    }

    private void sendRequest() {
        ToggleButton filter = (ToggleButton) getActivity().findViewById(R.id.buttonFilter);
        EditText inputView = (EditText) getActivity().findViewById(R.id.inputText);
        WordCountRequestTask wkrt = new WordCountRequestTask();
        if (filter.isChecked())
            wkrt.setIsFilterWords(WordCountRequestTask.TRUE);
        wkrt.setRequestText(inputView.getEditableText().toString());
        wkrt.execute(this);
    }

    private void showResult(Map<String, Integer> countResult){
        TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.resultTable);
        tableLayout.removeAllViews();

        final TextView sortBtnWord = new TextView(getActivity());
        final TextView sortBtnCount = new TextView(getActivity());

        TableRow tableRow = new TableRow(getActivity());
        tableRow.addView(sortBtnWord);
        tableRow.addView(sortBtnCount);
        tableRow.setBackgroundColor(Color.parseColor("#cccccc"));
        tableRow.setPadding(5, 5, 5, 5);
        tableLayout.addView(tableRow);

        for (Map.Entry<String, Integer> entry: countResult.entrySet())
        {
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

    private void hideError(){
        ListView lvMain = (ListView) getActivity().findViewById(R.id.errorList);
        if (lvMain.getAdapter() != null && !lvMain.getAdapter().isEmpty()){
            ((ArrayAdapter<String>)lvMain.getAdapter()).clear();
        }
    }

    private void showError(List<String> errorList){
        ListView lvMain = (ListView) getActivity().findViewById(R.id.errorList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.error_list_item, errorList);
        lvMain.setAdapter(adapter);
    }
}
