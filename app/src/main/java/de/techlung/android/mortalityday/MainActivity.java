package de.techlung.android.mortalityday;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.techlung.android.mortalityday.baasbox.BaasBoxMortalityDay;
import de.techlung.android.mortalityday.gathering.GatheringViewController;
import de.techlung.android.mortalityday.login.LoginFragment;
import de.techlung.android.mortalityday.messages.MessageManager;
import de.techlung.android.mortalityday.notification.MortalityDayNotificationManager;
import de.techlung.android.mortalityday.settings.Preferences;
import de.techlung.android.mortalityday.settings.PreferencesActivity;
import de.techlung.android.mortalityday.thoughts.ThoughtManager;
import de.techlung.android.mortalityday.thoughts.ThoughtsViewController;
import de.techlung.android.mortalityday.util.ToolBox;


public class MainActivity extends BaseActivity implements ThoughtManager.LocalThoughtsChangedListener, ThoughtManager.SharedThoughtsChangedListener {
    public static final String TAG = MainActivity.class.getName();
    public static final boolean DEBUG = false;

    public static final int TRANSITION_SPEED = 300;

    @Bind(R.id.main) DrawerLayout drawerLayout;
    @Bind(R.id.main_pager) ViewPager pager;

    @Bind(R.id.header_pagertabs_thoughts) View pagerTabThoughts;
    @Bind(R.id.header_pagertabs_gathering) View pagerTabGathering;
    @Bind(R.id.header_pagertabs_bar) View pagerTabBar;

    @Bind(R.id.header_menu) View headerMenuButton;
    @Bind(R.id.header_more) View headerMoreButton;

    @Bind(R.id.drawer_restore) View drawerRestore;
    @Bind(R.id.drawer_settings) View drawerSettings;
    @Bind(R.id.drawer_login) View drawerLogin;

    @Bind(R.id.message_container) RelativeLayout messageContainer;

    ThoughtsViewController thoughtsViewContoller;
    GatheringViewController gatheringViewController;

    @Override
    public void onLocalThoughtsChanged() {
        if (thoughtsViewContoller != null) {
            thoughtsViewContoller.reloadAdapter();
        }
    }

    @Override
    public void onSharedThoughtsChanged() {
        if (gatheringViewController != null) {
            gatheringViewController.reloadAdapter();
        }
    }

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

        checkFirstStart();

        ButterKnife.bind(this);

        initMenu();
        initDrawer();
        initViewPager();
        initMessage();

        ThoughtManager.addLocalThoughtsChangedListener(this);
        ThoughtManager.addSharedThoughtsChangedListener(this);
    }

    private void checkFirstStart() {
        if (Preferences.getFirstStart()) {
            startActivity(new Intent(this, PreferencesActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MortalityDayNotificationManager.setNextNotification(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThoughtManager.removeLocalThoughtsChangedListener(this);
        ThoughtManager.removeSharedThoughtsChangedListener(this);
    }

    private void initMenu() {
        headerMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        headerMoreButton.setVisibility(View.GONE);
        headerMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO open more menü to sort Data 
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
        drawerRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaasBoxMortalityDay.restoreThoughts();
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
                if (gatheringViewController != null) {
                    gatheringViewController.reloadData();
                }
            }
        });
    }

    private void initMessage() {
        MessageManager messageManager = new MessageManager(this, messageContainer);
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
            YoYo.with(Techniques.RotateOut).duration(TRANSITION_SPEED).playOn(headerMoreButton);
            // push load data
        } else {
            headerMoreButton.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.RotateIn).duration(TRANSITION_SPEED).playOn(headerMoreButton);
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
