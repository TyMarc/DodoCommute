package com.lesgens.dodocommute.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lesgens.dodocommute.R;
import com.lesgens.dodocommute.model.Location;
import com.lesgens.dodocommute.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by marc-antoinehinse on 2016-01-23.
 */
public class LocationsAdapter extends ArrayAdapter<Location> {
    private Context mContext;
    private LayoutInflater mInflater = null;

    private ArrayList<Location> locations;

    public LocationsAdapter(Context context, ArrayList<Location> locations) {
        super(context, R.layout.location_item, locations);
        mContext = context;
        this.locations = locations;
    }

    static class ViewHolder {
        public ImageView image;
        public TextView name;
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
            rowView = getInflater().inflate(R.layout.location_item, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) rowView.findViewById(R.id.location_name);
            holder.image = (ImageView) rowView.findViewById(R.id.location_image);
            rowView.setTag(holder);
        } else{
            rowView = convertView;
            holder = (ViewHolder) rowView.getTag();
        }

        final Location l = locations.get(position);

        holder.name.setText(l.getName());
        Picasso.with(getContext()).load(Utils.getGoogleMapImageUrl(l.getLocation())).fit().centerCrop().into(holder.image);

        return rowView;
    }
}