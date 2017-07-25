package com.bokun.bkjcb.chengtou;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.bokun.bkjcb.chengtou.Util.L;
import com.github.mikephil.charting.data.BarDataSet;
import com.rmondjone.locktableview.LockTableView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout parentView;
    BarDataSet set1;
    private LockTableView mLockTableView;
    private LinearLayout show;
    private Fragment mainFragment;
    private Fragment tableFragment;
    private Fragment selectFragment;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Fragment currentFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_main);

        mainFragment = new MainFragment();
        tableFragment = new TableFragment();
        selectFragment = new SelectFragment();

        currentFragment = mainFragment;

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.table_layout, currentFragment);
        transaction.commit();
        changTitle("城投计划——首页");
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_main) {
            if (currentFragment instanceof MainFragment) {
                L.i("yes");
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            currentFragment = mainFragment;
            changTitle("城投计划——首页");
        } else if (id == R.id.nav_lookout) {
            currentFragment = tableFragment;
            changTitle("城投计划——查看统计表");
        } else if (id == R.id.nav_select) {
            currentFragment = selectFragment;
            changTitle("城投计划——信息查询");
          /*  } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {*/

        } else if (id == R.id.nav_send) {
            finish();
        }
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.table_layout, currentFragment);
        transaction.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (currentFragment instanceof MainFragment) {
            navigationView.setCheckedItem(R.id.nav_main);
        } else if (currentFragment instanceof TableFragment) {
            navigationView.setCheckedItem(R.id.nav_lookout);
        } else {
            navigationView.setCheckedItem(R.id.nav_select);
        }
    }

    private void changTitle(String title) {
        toolbar.setTitle(title);
    }
}
