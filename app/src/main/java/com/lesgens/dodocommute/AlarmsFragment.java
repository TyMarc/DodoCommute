package com.lesgens.dodocommute;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lesgens.dodocommute.adapters.AlarmsAdapter;
import com.lesgens.dodocommute.db.DatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlarmsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView listView;
    private AlarmsAdapter adapter;

    public AlarmsFragment() {
        // Required empty public constructor
    }

    public static AlarmsFragment newInstance() {
        AlarmsFragment fragment = new AlarmsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_view, container, false);

        listView = (ListView) v.findViewById(R.id.list);
        listView.setOnItemClickListener(this);


        return v;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList() {
        adapter = new AlarmsAdapter(getActivity(), DatabaseHelper.getInstance().getAlarms());
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WizardActivity.show(getActivity(), adapter.getItem(position).getId());
    }
}
