package com.lesgens.dodocommute;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.lesgens.dodocommute.model.AlarmProfile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HoursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HoursFragment extends Fragment implements View.OnClickListener, TimePicker.OnTimeChangedListener {
    private TimePicker timePicker;

    public HoursFragment() {
        // Required empty public constructor
    }

    public static HoursFragment newInstance() {
        HoursFragment fragment = new HoursFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.hours, container, false);

        v.findViewById(R.id.next).setOnClickListener(this);
        v.findViewById(R.id.back).setOnClickListener(this);

        timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        if(getActivity() instanceof WizardActivity && ((WizardActivity) getActivity()).getAlarmProfile().getArrivalHour() != -1) {
            timePicker.setCurrentHour(((WizardActivity) getActivity()).getAlarmProfile().getArrivalHour());
        } else {
            timePicker.setCurrentHour(9);
            if(getActivity() instanceof WizardActivity) {
                ((WizardActivity) getActivity()).getAlarmProfile().setArrivalHour(9);
            }
        }

        if(getActivity() instanceof WizardActivity && ((WizardActivity) getActivity()).getAlarmProfile().getArrivalMinute() != -1) {
            timePicker.setCurrentMinute(((WizardActivity) getActivity()).getAlarmProfile().getArrivalMinute());
        } else {
            timePicker.setCurrentMinute(0);
            if(getActivity() instanceof WizardActivity) {
                ((WizardActivity) getActivity()).getAlarmProfile().setArrivalMinute(0);
            }
        }

        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.next) {
            if(getActivity() instanceof WizardActivity) {
                ((WizardActivity) getActivity()).nextPage();
            }
        } else if (id == R.id.back) {
            if(getActivity() instanceof WizardActivity) {
                ((WizardActivity) getActivity()).backPage();
            }
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        if(getActivity() instanceof WizardActivity) {
            ((WizardActivity) getActivity()).getAlarmProfile().setArrivalHour(hourOfDay);
            ((WizardActivity) getActivity()).getAlarmProfile().setArrivalMinute(minute);
        }
    }
}
