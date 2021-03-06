package com.lesgens.dodocommute;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lesgens.dodocommute.adapters.AlarmsAdapter;
import com.lesgens.dodocommute.adapters.LocationsAdapter;
import com.lesgens.dodocommute.db.DatabaseHelper;
import com.lesgens.dodocommute.model.AlarmProfile;
import com.lesgens.dodocommute.model.Location;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {
    private ListView listView;
    private LocationsAdapter adapter;

    public LocationsFragment() {
        // Required empty public constructor
    }

    public static LocationsFragment newInstance() {
        LocationsFragment fragment = new LocationsFragment();
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
        listView.setOnItemLongClickListener(this);


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
        adapter = new LocationsAdapter(getActivity(), DatabaseHelper.getInstance().getLocations());
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete)
                .setMessage(R.string.delete_location)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Location l = adapter.getItem(position);
                        DatabaseHelper.getInstance().deletePlaceById(l.getId());
                        adapter.remove(l);
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
        return true;
    }
}
