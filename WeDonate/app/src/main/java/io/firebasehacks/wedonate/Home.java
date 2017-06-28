package io.firebasehacks.wedonate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import io.firebasehacks.wedonate.activity.AddDonationActivity;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    public static final String KEY_USER_TYPE = "user_type";
    private int mUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        mUserType = getIntent().getIntExtra(KEY_USER_TYPE, Constants.USER_TYPE_INDIVIDUAL);
        if (mUserType == Constants.USER_TYPE_INDIVIDUAL) {
            navigationView.inflateMenu(R.menu.activity_home_donor_drawer);
        } else {
            navigationView.inflateMenu(R.menu.activity_home_org_drawer);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_all_donations:
                break;
            case R.id.nav_friends_donations:
                break;
            case R.id.nav_my_donations:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                handleFab();
                break;
        }
    }

    private void handleFab() {
        if (mUserType == Constants.USER_TYPE_INDIVIDUAL) {
            Intent intent = new Intent(this, AddDonationActivity.class);
            startActivity(intent);
        } else {

        }
    }
}
