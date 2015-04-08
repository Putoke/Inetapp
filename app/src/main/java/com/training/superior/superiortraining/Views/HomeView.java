package com.training.superior.superiortraining.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.training.superior.superiortraining.Controllers.HomeActivity;
import com.training.superior.superiortraining.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by joakim on 15-03-18.
 *
 */
public class HomeView {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    static HomeActivity activity;

    public HomeView(HomeActivity activity) {
        this.activity = activity;

        // Set up the action bar.
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(activity.getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) activity.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(activity));
        }
    }

    public void setCurrentItem(int item) {
        mViewPager.setCurrentItem(item);
    }

    public void updateScheduleJSON(JSONArray jsonArray) {
        List<Fragment> frags = activity.getSupportFragmentManager().getFragments();
        for (Fragment f : frags) {
            if(f instanceof ScheduleFragment)
                ((ScheduleFragment) f).updateSpinner(jsonArray);
        }
    }

    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return  ScheduleFragment.newInstance(1, new JSONArray());
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return activity.getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return activity.getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return activity.getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }
    }

    public static class ScheduleFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_JSON_ARRAY = "json_array";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ScheduleFragment newInstance(int sectionNumber, JSONArray jsonArray) {
            ScheduleFragment fragment = new ScheduleFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_JSON_ARRAY, jsonArray.toString());
            fragment.setArguments(args);
            return fragment;
        }

        public ScheduleFragment() {
        }

        public void updateSpinner(JSONArray jsonArray) {
            System.out.println("HEJ: " + jsonArray.toString());
            Spinner spinner = (Spinner) getView().findViewById(R.id.schedules_spinner);

            ArrayList<String> names = new ArrayList<>();

            for(int i=0; i<jsonArray.length(); i++){
                try {
                    names.add(jsonArray.getJSONObject(i).getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_layout, names);
            adapter.setDropDownViewResource(R.layout.spinner_layout);
            spinner.setAdapter(adapter);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

            return rootView;
        }


    }

}
