package com.training.superior.superiortraining.Controllers;

import java.net.CookieStore;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;

import com.training.superior.superiortraining.Models.ScheduleTask;
import com.training.superior.superiortraining.R;
import com.training.superior.superiortraining.Views.HomeView;

import org.json.JSONArray;

public class HomeActivity extends ActionBarActivity implements ActionBar.TabListener {

    HomeView view;
    ScheduleTask sTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sTask = new ScheduleTask();
        view = new HomeView(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CookieManager.getInstance().removeAllCookies(null);
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
        });
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
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        if(view != null) {
            view.setCurrentItem(tab.getPosition());
        }

        switch (tab.getPosition()) {
            case 0:
                System.out.println(getString(R.string.title_section1));
                sTask = new ScheduleTask();
                sTask.execute();
                try {
                    JSONArray jsonArray = sTask.get();
                    if(view != null)
                        view.updateScheduleJSON(jsonArray);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            case 1:
                System.out.println(getString(R.string.title_section2));
            case 2:
                System.out.println(getString(R.string.title_section3));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

}
