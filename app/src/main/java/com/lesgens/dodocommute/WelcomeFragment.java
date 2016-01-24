package com.lesgens.dodocommute;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment implements View.OnClickListener {

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.welcome, container, false);

        v.findViewById(R.id.next).setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.next) {
            if(getActivity() instanceof WizardActivity) {
                ((WizardActivity) getActivity()).nextPage();
            }
        }
    }
}
