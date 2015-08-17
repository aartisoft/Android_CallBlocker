package com.codersact.smsblock.main;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.codersact.smsblock.adapter.NavigationAdapter;
import com.codersact.smsblock.blacklist.BlackListFragment;
import com.codersact.smsblock.blockedsms.BlockedListFragment;
import com.codersact.smsblock.inbox.InboxFragment;
import com.codersact.smsblock.model.NavigationMenu;

import java.util.ArrayList;

import activity.masum.com.smsblock.R;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    RecyclerView navigationRecylerView;
    private CharSequence mTitle;
    DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    ArrayList<NavigationMenu> navigationMenus;

    //New
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initView();
        setClickListener();
        blackListFragment();
        getSupportActionBar().setTitle("Black List");

    }

    private void setClickListener() {
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        };

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        navigationRecylerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    mDrawerLayout.closeDrawers();

                    if (recyclerView.getChildPosition(child) == 1) {
                        blackListFragment();
                        setTitle(navigationMenus.get(1).getMenuName());

                    } else if (recyclerView.getChildPosition(child) == 2) {
                        logsFragment();
                        setTitle(navigationMenus.get(2).getMenuName());

                    } else if (recyclerView.getChildPosition(child) == 3) {
                        inboxFragment();
                        setTitle(navigationMenus.get(3).getMenuName());

                    } else if (recyclerView.getChildPosition(child) == 4) {
                        settingsFragment();
                        setTitle(navigationMenus.get(4).getMenuName());
                    }

                    //Toast.makeText(MainActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
                    return true;

                }


                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();

    }

    private void initView() {
        navigationMenus = new ArrayList<>();

        NavigationMenu navigationMenu = new NavigationMenu();
        navigationMenu.setMenuName("Header");
        navigationMenus.add(navigationMenu);

        NavigationMenu navigationMenu1 = new NavigationMenu();
        navigationMenu1.setMenuName("Black List");
        navigationMenus.add(navigationMenu1);

        NavigationMenu navigationMenu2 = new NavigationMenu();
        navigationMenu2.setMenuName("Log");
        navigationMenus.add(navigationMenu2);

        NavigationMenu navigationMenu3 = new NavigationMenu();
        navigationMenu3.setMenuName("Inbox");
        navigationMenus.add(navigationMenu3);

//        NavigationMenu navigationMenu4 = new NavigationMenu();
//        navigationMenu4.setMenuName("Settings");
//        navigationMenus.add(navigationMenu4);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationRecylerView = (RecyclerView) findViewById(R.id.listView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        navigationRecylerView.setLayoutManager(layoutManager);

        NavigationAdapter mAdapter = new NavigationAdapter(navigationMenus, getApplicationContext());
        navigationRecylerView.setAdapter(mAdapter);

    }

    private void logsFragment() {
        android.app.Fragment fragment = null;
        fragment = new BlockedListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        android.app.FragmentTransaction ft = frgManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment, "SEARCH_FRAGMENT");
        ft.commit();
    }

    private void blackListFragment() {
        android.app.Fragment fragment = null;
        fragment = new BlackListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        android.app.FragmentTransaction ft = frgManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment, "SEARCH_FRAGMENT");
        ft.commit();
    }

    private void inboxFragment() {
        android.app.Fragment fragment = null;
        fragment = new InboxFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        android.app.FragmentTransaction ft = frgManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment, "SEARCH_FRAGMENT");
        ft.commit();
    }

    private void settingsFragment() {
        android.app.Fragment fragment = null;
        fragment = new BlackListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentManager frgManager = getFragmentManager();
        android.app.FragmentTransaction ft = frgManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment, "SEARCH_FRAGMENT");
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.btnFetchSMS:
                *//*Intent intent = new Intent(this, BlackListActivity.class);
                startActivity(intent);*//*
                break;

            case R.id.btnBlockedList:
               *//* Intent intent1 = new Intent(this, BlockedListActivity.class);
                startActivity(intent1);*//*
                break;*/
        }
    }

    @Override
    public void setTitle(CharSequence title) {
       // mTitle = title;
        getSupportActionBar().setTitle(title);
    }

}
