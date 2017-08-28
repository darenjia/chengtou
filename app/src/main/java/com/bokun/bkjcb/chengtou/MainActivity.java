package com.bokun.bkjcb.chengtou;

import android.content.Context;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bokun.bkjcb.chengtou.Util.Constants;
import com.bokun.bkjcb.chengtou.Util.L;
import com.bokun.bkjcb.chengtou.Util.SPUtils;
import com.github.mikephil.charting.data.BarDataSet;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout parentView;
    BarDataSet set1;
    private LinearLayout show;
    private Fragment mainFragment;
    private Fragment tableFragment;
    private Fragment selectFragment;
    private FragmentTransaction transaction;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Fragment currentFragment;
    private Toolbar toolbar;
    private boolean opened;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setIp();

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

        loadFirstPage();

        setDrawerLayoutListener();
    }

    private void setDrawerLayoutListener() {
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                opened = false;
                InputMethodManager manager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                boolean isOpen = manager.isActive();
                if (isOpen) {//因为是在fragment下，所以用了getView()获取view，也可以用findViewById（）来获取父控件
                    drawer.requestFocus();//使其它view获取焦点.这里因为是在fragment下,所以便用了getView(),可以指定任意其它view
                    manager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    @Override
    public void onBackPressed() {
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            drawer.openDrawer(GravityCompat.START);
        }*/
        if (!drawer.isDrawerOpen(GravityCompat.START) && !opened) {
            drawer.openDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.START) && !opened) {
            drawer.closeDrawer(GravityCompat.START);
            opened = true;
        } else if (opened && currentFragment != mainFragment) {
            loadFirstPage();
        } else {
            if (System.currentTimeMillis() - time > 1000) {
                time = System.currentTimeMillis();
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
    }

    private void loadFirstPage() {
        currentFragment = mainFragment;
        try {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.table_layout, currentFragment);
            transaction.commit();
        } catch (IllegalStateException e) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.table_layout, currentFragment);
            transaction.commit();
        }
        changTitle("城投管理—首页");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId()) {
            case R.id.set_ip1:
                Constants.HTTP_URL = Constants.HTTPURL.replace("IP_ADDRESS", Constants.IP_1);
                Constants.HTTP_DERAIL_URL = Constants.GET_DETAIL_URL.replace("IP_ADDRESS", Constants.IP_1);
                Constants.HTTP_TABLE_URL = Constants.GET_TABLT_DATA_URL.replace("IP_ADDRESS", Constants.IP_1);
                Toast.makeText(this, "ip:" + Constants.IP_1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_ip2:
                Constants.HTTP_URL = Constants.HTTPURL.replace("IP_ADDRESS", Constants.IP_3);
                Constants.HTTP_DERAIL_URL = Constants.GET_DETAIL_URL.replace("IP_ADDRESS", Constants.IP_3);
                Constants.HTTP_TABLE_URL = Constants.GET_TABLT_DATA_URL.replace("IP_ADDRESS", Constants.IP_3);
                Toast.makeText(this, "ip:" + Constants.IP_3, Toast.LENGTH_SHORT).show();
                break;
        }*/
        return true;
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
            changTitle("城投管理—首页");
        } else if (id == R.id.nav_lookout) {
            currentFragment = tableFragment;
            changTitle("城投管理—查看统计表");
        } else if (id == R.id.nav_select) {
            currentFragment = selectFragment;
            changTitle("城投管理—信息查询");
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

    private void setIp() {
        String ip = (String) SPUtils.get(this, "IP", Constants.IP_1);
        Constants.HTTP_URL = Constants.HTTPURL.replace("IP_ADDRESS", ip);
        Constants.HTTP_DERAIL_URL = Constants.GET_DETAIL_URL.replace("IP_ADDRESS", ip);
        Constants.HTTP_TABLE_URL = Constants.GET_TABLT_DATA_URL.replace("IP_ADDRESS", ip);
    }
}
