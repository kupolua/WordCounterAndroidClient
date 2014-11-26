package tasks;

import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableBuilder {
    public
    TableLayout tableLayout = new TableLayout(this);
    tableLayout.setLayoutParams(new TableLayout.LayoutParams(
    ViewGroup.LayoutParams.FILL_PARENT,
    ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    tableLayout.setStretchAllColumns(true);

    TextView textView1 = new TextView(this);
    textView1.setText("Column 1");
    TextView textView2 = new TextView(this);
    textView2.setText("Column 2");
    TextView textView3 = new TextView(this);
    textView3.setText("Column 3");

    TextView textView4 = new TextView(this);
    textView4.setText("Column 4");
    TextView textView5 = new TextView(this);
    textView5.setText("Column 5");
    TextView textView6 = new TextView(this);
    textView6.setText("Column 6");

    TextView textView7 = new TextView(this);
    textView7.setText("Column 7");
    TextView textView8 = new TextView(this);
    textView8.setText("Column 8");
    TextView textView9 = new TextView(this);
    textView9.setText("Column 9");

    TableRow tableRow1 = new TableRow(this);
    TableRow tableRow2 = new TableRow(this);
    TableRow tableRow3 = new TableRow(this);

    tableRow1.addView(textView1);
    tableRow1.addView(textView2);
    tableRow1.addView(textView3);

    tableRow2.setBackgroundColor(0xffcccccc);
    tableRow2.addView(textView4);
    tableRow2.addView(textView5);
    tableRow2.addView(textView6);

    tableRow3.addView(textView7);
    tableRow3.addView(textView8);
    tableRow3.addView(textView9);

    tableLayout.addView(tableRow1);
    tableLayout.addView(tableRow2);
    tableLayout.addView(tableRow3);

    setContentView(tableLayout);
}
