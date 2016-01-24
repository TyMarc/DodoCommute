package com.lesgens.dodocommute;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineFragment extends Fragment implements View.OnClickListener, TimePicker.OnTimeChangedListener {
    private TimePicker timePicker;

    public RoutineFragment() {
        // Required empty public constructor
    }

    public static RoutineFragment newInstance() {
        RoutineFragment fragment = new RoutineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.routine, container, false);

        v.findViewById(R.id.next).setOnClickListener(this);
        v.findViewById(R.id.back).setOnClickListener(this);

        timePicker = (TimePicker) v.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        if(getActivity() instanceof WizardActivity && ((WizardActivity) getActivity()).getAlarmProfile().getRoutineHour() != -1) {
            timePicker.setCurrentHour(((WizardActivity) getActivity()).getAlarmProfile().getRoutineHour());
        } else {
            timePicker.setCurrentHour(0);
        }

        if(getActivity() instanceof WizardActivity && ((WizardActivity) getActivity()).getAlarmProfile().getRoutineMinute() != -1) {
            timePicker.setCurrentMinute(((WizardActivity) getActivity()).getAlarmProfile().getRoutineMinute());
        } else {
            timePicker.setCurrentMinute(45);
        }
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
            ((WizardActivity) getActivity()).getAlarmProfile().setRoutineHour(hourOfDay);
            ((WizardActivity) getActivity()).getAlarmProfile().setRoutineMinute(minute);
        }
    }
}
