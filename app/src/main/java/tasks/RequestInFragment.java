package tasks;

import android.support.v4.app.FragmentActivity;

public interface RequestInFragment {

    public void startExecute(WordCountRequestTask wkrt);

    public void finishExecute(WordCountRequestTask wkrt);

    public FragmentActivity getActivity();
}
