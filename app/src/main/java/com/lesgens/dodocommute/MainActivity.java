package com.lesgens.dodocommute;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.lesgens.dodocommute.utils.PreferencesController;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MainActivity";

    private ArrayList<Fragment> fragments;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;


    public static void show(final Context context) {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fragments = new ArrayList<Fragment>();
        fragments.add(AlarmsFragment.newInstance());
        fragments.add(LocationsFragment.newInstance());

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        findViewById(R.id.button_alarm).setOnClickListener(this);
        findViewById(R.id.button_location).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PreferencesController.isFirstUse(this)) {
            WizardActivity.show(this);
        }
    }

    @Override
    public void onBackPressed() {
        if(findViewById(R.id.multiple_actions_down) != null && ((FloatingActionsMenu) findViewById(R.id.multiple_actions_down)).isExpanded()) {
            ((FloatingActionsMenu) findViewById(R.id.multiple_actions_down)).collapse();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.button_alarm) {
            WizardActivity.show(this);
        } else if (id == R.id.button_location) {
            AddLocationActivity.show(this);
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "Alarms" : "Locations";
        }
    }

}
