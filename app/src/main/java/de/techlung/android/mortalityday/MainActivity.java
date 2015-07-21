package de.techlung.android.mortalityday;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.baasbox.android.BaasDocument;
import com.baasbox.android.BaasHandler;
import com.baasbox.android.BaasResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.techlung.android.mortalityday.baasbox.Constants;
import de.techlung.android.mortalityday.gathering.GatheringViewController;
import de.techlung.android.mortalityday.login.LoginFragment;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.settings.PreferencesActivity;
import de.techlung.android.mortalityday.thoughts.ThoughtsViewController;
import de.techlung.android.mortalityday.util.DeviceUtil;
import de.techlung.android.mortalityday.util.Toaster;
import de.techlung.android.mortalityday.util.ToolBox;


public class MainActivity extends BaseActivity {
    public static final String TAG = MainActivity.class.getName();
    public static final boolean DEBUG = false;

    public static final int TRANSITION_SPEED = 300;

    @Bind(R.id.main) DrawerLayout drawerLayout;
    @Bind(R.id.main_pager) ViewPager pager;

    @Bind(R.id.header_pagertabs_thoughts) View pagerTabThoughts;
    @Bind(R.id.header_pagertabs_gathering) View pagerTabGathering;
    @Bind(R.id.header_pagertabs_bar) View pagerTabBar;

    @Bind(R.id.header_menu) View headerMenuButton;

    @Bind(R.id.drawer_settings) View drawerSettings;
    @Bind(R.id.drawer_login) View drawerLogin;

    ThoughtsViewController thoughtsViewContoller;
    GatheringViewController gatheringViewController;

    public enum State {
        THOUGHTS, GATHERING
    }
    public State currentState = State.THOUGHTS;

    private static MainActivity instance;
    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        setContentView(R.layout.main_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);

        initDrawer();
        initViewPager();
        initBackend();

    }

    private void initBackend() {

        BaasDocument document = new BaasDocument(Constants.COLLECTION_DEVICE);
        document.put(Constants.COLLECTION_DEVICE_ID, Preferences.getDeviceId());
        document.put(Constants.COLLECTION_DEVICE_NAME, Preferences.getUserName());
        document.save(new BaasHandler<BaasDocument>() {
            @Override
            public void handle(BaasResult<BaasDocument> baasResult) {
                Log.e(TAG, baasResult.toString());
                Toaster.show(baasResult.toString());
            }
        });
    }

    private void initDrawer() {
        drawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
            }
        });

        headerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        drawerSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
            }
        });
        drawerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(new LoginFragment(), LoginFragment.TAG).commit();
            }
        });
    }

    private void initViewPager() {
        pager = (ViewPager) findViewById(R.id.main_pager);
        pager.setAdapter(new MyAdapter());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    changeState(State.THOUGHTS);
                } else {
                    changeState(State.GATHERING);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pagerTabThoughts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
            }
        });
        pagerTabGathering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });
    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view;

            if (position == 0) {
                thoughtsViewContoller = new ThoughtsViewController(container);
                view = thoughtsViewContoller.getView();
            } else {
                gatheringViewController = new GatheringViewController(container);
                view = gatheringViewController.getView();
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void changeState(State state) {
        this.currentState = state;

        animateTabBarToState(state);

        if (state == State.THOUGHTS) {
            // push load data
        } else {
            // push load data
        }
    }

    private void animateTabBarToState(State state) {
        if (state == State.THOUGHTS) {
            pagerTabBar.animate().translationX(0).setDuration(TRANSITION_SPEED / 2);
        } else {
            pagerTabBar.animate().translationX(ToolBox.getScreenWidthPx(MainActivity.getInstance()) / 2).setDuration(TRANSITION_SPEED / 2);
        }
    }


}
