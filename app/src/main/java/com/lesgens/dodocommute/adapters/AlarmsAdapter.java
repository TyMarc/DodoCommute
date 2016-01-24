package com.lesgens.dodocommute.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.lesgens.dodocommute.R;
import com.lesgens.dodocommute.db.DatabaseHelper;
import com.lesgens.dodocommute.model.AlarmProfile;
import com.lesgens.dodocommute.utils.DirectionsUtils;
import com.lesgens.dodocommute.utils.Utils;

import java.util.ArrayList;

/**
 * Created by marc-antoinehinse on 2016-01-23.
 */
public class AlarmsAdapter extends ArrayAdapter<AlarmProfile> {
    private Context mContext;
    private LayoutInflater mInflater = null;

    private ArrayList<AlarmProfile> alarms;

    public AlarmsAdapter(Context context, ArrayList<AlarmProfile> alarms) {
        super(context, R.layout.alarm_item, alarms);
        mContext = context;
        this.alarms = alarms;
    }

    static class ViewHolder {
        public Switch alarmSwitch;
        public TextView alarmName;
        public TextView timeToWake;
    }

    private LayoutInflater getInflater(){
        if(mInflater == null)
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return mInflater;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;

        final ViewHolder holder;
        if(convertView == null){ // Only inflating if necessary is great for performance
            rowView = getInflater().inflate(R.layout.alarm_item, parent, false);

            holder = new ViewHolder();
            holder.alarmName = (TextView) rowView.findViewById(R.id.alarm_name);
            holder.timeToWake = (TextView) rowView.findViewById(R.id.time_to_wake_up);
            holder.alarmSwitch = (Switch) rowView.findViewById(R.id.switch_alarm);
            rowView.setTag(holder);
        } else{
            rowView = convertView;
            holder = (ViewHolder) rowView.getTag();
        }

        final AlarmProfile ap = alarms.get(position);

        holder.alarmName.setText(ap.getName());
        holder.alarmSwitch.setChecked(ap.isOn());
        holder.timeToWake.setText(ap.getTimeToWakeUpAsText());

        holder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ap.setIsOn(isChecked);
                if (isChecked) {
                    DirectionsUtils.getInstance().execute(getContext(), ap, new DirectionsUtils.DirectionsListener() {
                        @Override
                        public void onSuccess(AlarmProfile alarmProfile) {
                            DatabaseHelper.getInstance().addOrUpdateAlarm(ap);
                            holder.timeToWake.setText(ap.getTimeToWakeUpAsText());
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
                } else {
                    DatabaseHelper.getInstance().addOrUpdateAlarm(ap);
                }

            }
        });

        return rowView;
    }
}