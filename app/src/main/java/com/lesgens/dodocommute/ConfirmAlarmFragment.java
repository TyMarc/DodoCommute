package com.lesgens.dodocommute;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.lesgens.dodocommute.model.AlarmProfile;
import com.lesgens.dodocommute.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmAlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmAlarmFragment extends Fragment implements View.OnClickListener {

    public ConfirmAlarmFragment() {
        // Required empty public constructor
    }

    public static ConfirmAlarmFragment newInstance() {
        ConfirmAlarmFragment fragment = new ConfirmAlarmFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.confirm, container, false);

        if(getActivity() instanceof WizardActivity) {
            if(((WizardActivity) getActivity()).getAlarmProfile().getName() != null) {
                ((EditText) v.findViewById(R.id.alarm_name)).setText(((WizardActivity) getActivity()).getAlarmProfile().getName());
            }
        }

        v.findViewById(R.id.done).setOnClickListener(this);
        v.findViewById(R.id.back).setOnClickListener(this);

        return v;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if(menuVisible) {
            if(getActivity() != null && getActivity() instanceof WizardActivity && getView() != null)  {
                // ToDo take action when missing something
                final AlarmProfile ap = ((WizardActivity) getActivity()).getAlarmProfile();
                if(ap.getOrigin() != null) {
                    Picasso.with(getActivity()).load(Utils.getGoogleMapImageUrl(ap.getOrigin())).fit().centerCrop().into((ImageView) getView().findViewById(R.id.starting_position));
                }
                if(ap.getDestination() != null) {
                    Picasso.with(getActivity()).load(Utils.getGoogleMapImageUrl(ap.getDestination())).fit().centerCrop().into((ImageView) getView().findViewById(R.id.destination_position));
                }

                ((TextView) getView().findViewById(R.id.routine_time)).setText(Utils.getTimeAsText(ap.getRoutineHour(), ap.getRoutineMinute()));
                ((TextView) getView().findViewById(R.id.arrival_time)).setText(Utils.getTimeAsText(ap.getArrivalHour(), ap.getArrivalMinute()));
            }
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.done) {
            if(getActivity() instanceof WizardActivity) {
                String name = ((EditText) getView().findViewById(R.id.alarm_name)).getText().toString();
                if(name.isEmpty()) {
                    name = "New Alarm";
                }
                ((WizardActivity) getActivity()).getAlarmProfile().setName(name);
                ((WizardActivity) getActivity()).doneWizard();
            }
        } else if (id == R.id.back) {
            if(getActivity() instanceof WizardActivity) {
                ((WizardActivity) getActivity()).backPage();
            }
        }
    }


}
