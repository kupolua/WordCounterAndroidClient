package com.qalight.javacourse.wordcounterandroidclient.acceptanceTest.utils;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.qalight.javacourse.wordcounterandroidclient.R;
import com.robotium.solo.Solo;

import java.util.LinkedHashMap;
import java.util.Map;

public class Util {

    public static Map<String, String> getTableResult(Solo solo) {
        final int firstElement = 0;
        final int secondElement = 1;
        TableRow tableRow;

        Map<String, String> tableResult = new LinkedHashMap<String, String>();
        TableLayout tableLayout = (TableLayout) solo.getView(R.id.resultTable);

        for(int i = 1; i < tableLayout.getChildCount(); i++) {
            tableRow = (TableRow) tableLayout.getChildAt(i);
            String key = ((TextView) tableRow.getChildAt(firstElement)).getText().toString();
            String value = ((TextView) tableRow.getChildAt(secondElement)).getText().toString();
            tableResult.put(key, value);
        }
        return tableResult;
    }
}
