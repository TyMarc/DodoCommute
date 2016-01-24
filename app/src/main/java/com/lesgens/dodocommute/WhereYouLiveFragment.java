package com.lesgens.dodocommute;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lesgens.dodocommute.db.DatabaseHelper;
import com.lesgens.dodocommute.model.AlarmProfile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WhereYouLiveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhereYouLiveFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener, GoogleMap.OnMarkerClickListener {
    private static final String TAG = "WhereYouLiveFragment";
    private GoogleMap googleMap;
    private SupportMapFragment supportMapFragment;
    private AlertDialog dialog;
    private boolean isFirstPage;
    private Marker marker;

    public WhereYouLiveFragment() {
        // Required empty public constructor
    }

    public static WhereYouLiveFragment newInstance(final boolean isFirstPage) {
        WhereYouLiveFragment fragment = new WhereYouLiveFragment();
        Bundle b = new Bundle();
        b.putBoolean("isFirstPage", isFirstPage);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        supportMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, supportMapFragment)
                    .commit();
        }
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.where_you_live, container, false);

        isFirstPage = getArguments().getBoolean("isFirstPage", false);

        if(isFirstPage) {
            v.findViewById(R.id.back).setVisibility(View.GONE);
        } else {
            v.findViewById(R.id.back).setOnClickListener(this);
        }

        v.findViewById(R.id.next).setOnClickListener(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "onMapReady");
        this.googleMap = googleMap;
        this.googleMap.setOnMapClickListener(this);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.setMyLocationEnabled(true);

        if(getActivity() instanceof WizardActivity) {
            Log.i(TAG, "onMapReady: got my location");
            AlarmProfile ap = ((WizardActivity) getActivity()).getAlarmProfile();
            if(ap.getOrigin() != null) {
                LatLng latLng = new LatLng(ap.getOrigin().latitude, ap.getOrigin().longitude);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(16).build();
                this.googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getActivity().getResources().getString(R.string.origin)));
            } else {
                performMyLocationClick();
            }
        }

        for(com.lesgens.dodocommute.model.Location l : DatabaseHelper.getInstance().getLocations()) {
            this.googleMap.addMarker(new MarkerOptions()
                    .position(l.getLocation())
                    .title(l.getName()));
        }

        this.googleMap.setOnMarkerClickListener(this);

    }

    public void performMyLocationClick() {
        View v = getView().findViewById(Integer.parseInt("1"));
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
        showDialogLocationUse(latLng);
    }

    private void showDialogLocationUse(final LatLng latLng) {
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

        dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.origin)
                .setMessage(R.string.home_desc)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(marker != null) {
                            marker.remove();
                        }
                        marker = googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(getActivity().getResources().getString(R.string.origin)));
                        if(getActivity() instanceof WizardActivity) {
                            ((WizardActivity) getActivity()).getAlarmProfile().setOrigin(latLng);
                            ((WizardActivity) getActivity()).nextPage();
                        }
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
    public boolean onMarkerClick(Marker marker) {
        showDialogLocationUse(marker.getPosition());
        return true;
    }
}
