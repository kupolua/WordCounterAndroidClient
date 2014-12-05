package com.qalight.javacourse.wordcounterandroidclient;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
                getResult();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).setTitle(R.string.title_home);
    }

    private void getResult() {
        EditText inputView = (EditText) getActivity().findViewById(R.id.inputText);
        WordCountRequestTask<MainActivity> wkrt = new WordCountRequestTask<MainActivity>();
        wkrt.setRequestText(inputView.getEditableText().toString());
        wkrt.execute((MainActivity) getActivity());
    }

    @Override
    public void startExecute() {

    }

    @Override
    public void finishExecute() {

    }
}
