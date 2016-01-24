package com.lesgens.dodocommute;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lesgens.dodocommute.db.DatabaseHelper;
import com.lesgens.dodocommute.model.AlarmProfile;
import com.lesgens.dodocommute.utils.DirectionsUtils;
import com.lesgens.dodocommute.utils.PreferencesController;

import java.util.ArrayList;

public class WizardActivity extends AppCompatActivity {
    private static final String TAG = "WizardActivity";
    private ViewPager pager;
    private PagerAdapter adapter;
    private ArrayList<Fragment> fragments;
    private boolean isFirstTime;
    private ArrayList<ImageView> dots;
    private AlarmProfile alarmProfile;

    public static final void show(final Context context) {
        Intent i = new Intent(context, WizardActivity.class);
        i.putExtra("isFirstTime", PreferencesController.isFirstUse(context));
        context.startActivity(i);
    }

    public static final void show(final Context context, final long alarmId) {
        Intent i = new Intent(context, WizardActivity.class);
        i.putExtra("isFirstTime", PreferencesController.isFirstUse(context));
        i.putExtra("alarmId", alarmId);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wizard);

        final long alarmId = getIntent().getLongExtra("alarmId", -1);
        if(alarmId != -1) {
            alarmProfile = DatabaseHelper.getInstance().getAlarmById(alarmId);
        } else {
            alarmProfile = new AlarmProfile();
        }

        pager = (ViewPager) findViewById(R.id.pager);

        fragments = new ArrayList<Fragment>();

        isFirstTime = getIntent().getBooleanExtra("isFirstTime", false);

        if(isFirstTime) {
            fragments.add(WelcomeFragment.newInstance());
        }

        fragments.add(WhereYouLiveFragment.newInstance(!isFirstTime));
        fragments.add(WhereYouGoFragment.newInstance());
        fragments.add(HoursFragment.newInstance());
        fragments.add(RoutineFragment.newInstance());
        fragments.add(ConfirmAlarmFragment.newInstance());

        adapter = new WizardPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        addDots();
        selectDot(0);
    }



    private class WizardPagerAdapter extends FragmentStatePagerAdapter {
        public WizardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public void addDots() {
        dots = new ArrayList<>();
        LinearLayout dotsLayout = (LinearLayout)findViewById(R.id.dots);

        for(int i = 0; i < fragments.size(); i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.pager_dot_not_selected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            dotsLayout.addView(dot, params);

            dots.add(dot);
        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for(int i = 0; i < fragments.size(); i++) {
            int drawableId = (i==idx)?(R.drawable.pager_dot_selected):(R.drawable.pager_dot_not_selected);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    public void nextPage(){
        if(pager.getCurrentItem() < fragments.size() -1) {
            pager.setCurrentItem(pager.getCurrentItem() + 1, true);
        }
    }

    public void backPage() {
        if(pager.getCurrentItem() > 0) {
            pager.setCurrentItem(pager.getCurrentItem() - 1, true);
        }
    }

    @Override
    public void onBackPressed() {
        if(pager != null && pager.getCurrentItem() > 0) {
            backPage();
        } else {
            super.onBackPressed();
        }
    }

    public void doneWizard() {
        Log.i(TAG, alarmProfile.toString());
        DirectionsUtils.getInstance().execute(this, alarmProfile, new DirectionsUtils.DirectionsListener() {
            @Override
            public void onSuccess(AlarmProfile alarmProfile) {
                Log.i(TAG, "Time to wake up=" + alarmProfile.getTimeToWakeUpAsText());
                DatabaseHelper.getInstance().addOrUpdateAlarm(alarmProfile);
                PreferencesController.setPreference(WizardActivity.this, PreferencesController.FIRST_TIME_USE, false);
                finish();
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public AlarmProfile getAlarmProfile() {
        return alarmProfile;
    }
}
