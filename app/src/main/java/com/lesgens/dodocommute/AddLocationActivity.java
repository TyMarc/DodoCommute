package com.lesgens.dodocommute;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lesgens.dodocommute.adapters.AlarmsAdapter;
import com.lesgens.dodocommute.db.DatabaseHelper;
import com.lesgens.dodocommute.model.AlarmProfile;
import com.lesgens.dodocommute.utils.DirectionsUtils;
import com.lesgens.dodocommute.utils.PreferencesController;
import com.lesgens.dodocommute.utils.Utils;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private final static String TAG = "AddLocationActivity";
    private GoogleMap googleMap;
    private AlertDialog dialog;

    public static void show(final Context context) {
        Intent i = new Intent(context, AddLocationActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.setMyLocationEnabled(true);

        performMyLocationClick();
    }

    public void performMyLocationClick() {
        View v = findViewById(Integer.parseInt("1"));
        if(v != null) {
            final View parent = (View) v.getParent();
            if(parent != null) {
                final View myLocationButton = parent.findViewById(Integer.parseInt("2"));

                if(myLocationButton != null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myLocationButton.performClick();
                        }
                    }, 1500);
                }
            }
        }
    }

    @Override
    public void onMapClick(final LatLng latLng) {
        Log.i(TAG, "onMapLongClick:");
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

        final EditText input = new EditText(this);
        input.setHint("Location name");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        final int padding = Utils.dpInPixels(this, 20);
        input.setPadding(padding, padding, padding, padding);
        dialog = new AlertDialog.Builder(this)
                .setTitle("Location")
                .setMessage("Enter a name for this location")
                .setView(input)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper.getInstance().addPlace(input.getText().toString(), latLng);
                        finish();
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
    }
}
