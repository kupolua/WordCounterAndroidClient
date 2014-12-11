package tasks;

import android.support.v4.app.FragmentActivity;

public interface RequestInFragment {

    public void startExecute(WordCountRequestTask requestTask);

    public void finishExecute(WordCountRequestTask requestTask);

    public FragmentActivity getActivity();
}
