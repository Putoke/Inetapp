package com.training.superior.superiortraining.Views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Spinner;

import com.training.superior.superiortraining.Controllers.HomeActivity;
import com.training.superior.superiortraining.Models.AddExerciseTask;
import com.training.superior.superiortraining.Models.AddScheduleTask;
import com.training.superior.superiortraining.Models.AddWorkoutTask;
import com.training.superior.superiortraining.Models.ExerciseTask;
import com.training.superior.superiortraining.Models.RemoveExerciseTask;
import com.training.superior.superiortraining.Models.RemoveWorkoutTask;
import com.training.superior.superiortraining.Models.ScheduleTask;
import com.training.superior.superiortraining.Models.WorkoutTask;
import com.training.superior.superiortraining.R;
import com.training.superior.superiortraining.Views.adapters.ExpListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

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
                case 1:
                    return AddFragment.newInstance(2);
                case 2:
                    return  RemoveFragment.newInstance(3);
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

    public static class RemoveFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static RemoveFragment newInstance(int sectionNumber) {
            RemoveFragment fragment = new RemoveFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public RemoveFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_remove, container, false);

            final Spinner spinnerino = new Spinner(activity);
            String[] alternatives = new String[] {"Exercise", "Workout", "Schedule"};
            ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.spinner_layout, Arrays.asList(alternatives));
            spinnerino.setAdapter(adapter);
            final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.removefrag);

            spinnerino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = spinnerino.getSelectedItem().toString();
                    if(selectedItem.equalsIgnoreCase("exercise")){
                        removeExerciseView(layout, spinnerino);
                    } else if(selectedItem.equalsIgnoreCase("workout")) {
                        removeWorkoutView(layout, spinnerino);
                    } else if(selectedItem.equalsIgnoreCase("schedule")) {
                        removeScheduleView(layout, spinnerino);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            layout.addView(spinnerino);
            removeExerciseView(layout, spinnerino);

            return rootView;
        }

        private void removeScheduleView(LinearLayout layout, Spinner spinnerino) {
            final EditText name = new EditText(activity);
            name.setHint("Name");

            Button remove = new Button(activity);
            remove.setHint("Remove schedule");

            layout.removeAllViews();
            layout.addView(spinnerino);
            layout.addView(name);
            layout.addView(remove);
        }

        private void removeWorkoutView(LinearLayout layout, Spinner spinnerino) {
            final EditText name = new EditText(activity);
            name.setHint("Name");

            Button remove = new Button(activity);
            remove.setHint("Remove workout");

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveWorkoutTask rTask = new RemoveWorkoutTask(name.getText().toString());
                    rTask.execute();
                    name.setText("");
                    new AlertDialog.Builder(activity)
                            .setMessage("Workout removed")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            });

            layout.removeAllViews();
            layout.addView(spinnerino);
            layout.addView(name);
            layout.addView(remove);
        }

        private void removeExerciseView(LinearLayout layout, Spinner spinnerino) {
            final EditText name = new EditText(activity);
            name.setHint("Name");

            Button remove = new Button(activity);
            remove.setHint("Remove exercise");

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveExerciseTask rTask = new RemoveExerciseTask(name.getText().toString());
                    rTask.execute();
                    name.setText("");
                    new AlertDialog.Builder(activity)
                            .setMessage("Exercise removed")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            });

            layout.removeAllViews();
            layout.addView(spinnerino);
            layout.addView(name);
            layout.addView(remove);
        }
    }

    public static class AddFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AddFragment newInstance(int sectionNumber) {
            AddFragment fragment = new AddFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public AddFragment() {
        }

        private void createScheduleView (LinearLayout layout, Spinner spinner) {
            final EditText name = new EditText(activity);
            name.setHint("Name");
            final Spinner workout = new Spinner(activity);
            List<String> workouts = new ArrayList<>();

            WorkoutTask wTask = new WorkoutTask();
            wTask.execute();
            try {
                JSONArray workoutData = wTask.get();
                for(int i=0; i<workoutData.length(); i++) {
                    JSONObject ex = workoutData.getJSONObject(i);
                    workouts.add(ex.getString("name"));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_layout, workouts);
            workout.setAdapter(adapter);
            
            
            final EditText day = new EditText(activity);
            day.setHint("Day");

            Button create = new Button(activity);
            create.setHint("Create schedule");
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddScheduleTask aTask = new AddScheduleTask(name.getText().toString(), workout.getSelectedItem().toString(), day.getText().toString());
                    aTask.execute();
                    name.setText("");
                    day.setText("");
                    new AlertDialog.Builder(activity)
                            .setMessage("Schedule added")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            });

            layout.removeAllViews();
            layout.addView(spinner);
            layout.addView(name);
            layout.addView(workout);
            layout.addView(day);
            layout.addView(create);
        }

        private void createExerciseView (LinearLayout layout, Spinner spinner) {
            final EditText name = new EditText(activity);
            name.setHint("Name");
            final EditText muscleGroup = new EditText(activity);
            muscleGroup.setHint("Musclegroup");

            Button create = new Button(activity);
            create.setHint("Create exercise");
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddExerciseTask aTask = new AddExerciseTask(name.getText().toString(), muscleGroup.getText().toString());
                    aTask.execute();
                    name.setText("");
                    muscleGroup.setText("");
                    new AlertDialog.Builder(activity)
                            .setMessage("Exercise added")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            });

            layout.removeAllViews();
            layout.addView(spinner);
            layout.addView(name);
            layout.addView(muscleGroup);
            layout.addView(create);
        }

        private void createWorkoutView (LinearLayout layout, Spinner spinner) {
            final EditText name = new EditText(activity);
            name.setHint("Name");
            final Spinner exercise = new Spinner(activity);
            List<String> exercises = new ArrayList<>();

            ExerciseTask wTask = new ExerciseTask();
            wTask.execute();
            try {
                JSONArray workoutData = wTask.get();
                for(int i=0; i<workoutData.length(); i++) {
                    JSONObject ex = workoutData.getJSONObject(i);
                    exercises.add(ex.getString("name"));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_layout, exercises);
            exercise.setAdapter(adapter);


            final EditText sets = new EditText(activity);
            sets.setHint("Sets");
            final EditText reps = new EditText(activity);
            reps.setHint("Reps");

            Button create = new Button(activity);
            create.setHint("Create workout");
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddWorkoutTask aTask = new AddWorkoutTask(name.getText().toString(), exercise.getSelectedItem().toString(), sets.getText().toString(), reps.getText().toString());
                    aTask.execute();
                    name.setText("");
                    sets.setText("");
                    reps.setText("");
                    new AlertDialog.Builder(activity)
                            .setMessage("Workout added")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();
                }
            });

            layout.removeAllViews();
            layout.addView(spinner);
            layout.addView(name);
            layout.addView(exercise);
            layout.addView(sets);
            layout.addView(reps);
            layout.addView(create);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.add_layout, container, false);

            final Spinner spinnerino = new Spinner(activity);
            String[] alternatives = new String[] {"Exercise", "Workout", "Schedule"};
            ArrayAdapter adapter = new ArrayAdapter(activity, R.layout.spinner_layout, Arrays.asList(alternatives));
            spinnerino.setAdapter(adapter);
            final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.addfrag);

            spinnerino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = spinnerino.getSelectedItem().toString();
                    if(selectedItem.equalsIgnoreCase("exercise")){
                        createExerciseView(layout, spinnerino);
                    } else if(selectedItem.equalsIgnoreCase("workout")) {
                        createWorkoutView(layout, spinnerino);
                    } else if(selectedItem.equalsIgnoreCase("schedule")) {
                        createScheduleView(layout, spinnerino);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            layout.addView(spinnerino);
            createExerciseView(layout, spinnerino);

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

        JSONArray scheduleData;
        JSONArray workoutData;

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
            scheduleData = jsonArray;
            Spinner spinner = (Spinner) getView().findViewById(R.id.schedules_spinner);

            ArrayList<String> names = new ArrayList<>();

            for(int i=0; i<jsonArray.length(); i++){
                try {
                    if(!names.contains(jsonArray.getJSONObject(i).getString("name")))
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
            final View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
            final Spinner spinner = (Spinner) rootView.findViewById(R.id.schedules_spinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final ArrayList<String> workouts = new ArrayList<String>();
                    for(int i=0; i<scheduleData.length(); i++){
                        try {
                            String selectedItem = spinner.getSelectedItem().toString();
                            JSONObject sched = scheduleData.getJSONObject(i);
                            String name = sched.getString("name");


                            if(name.equals(selectedItem)) {
                                JSONArray wos = sched.getJSONArray("schedules");
                                for (int j = 0; j < wos.length(); j++) {
                                    JSONObject row = wos.getJSONObject(j);
                                    workouts.add(row.getString("workout") + " (" + row.getString("day") + ") ");
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    WorkoutTask workoutTask = new WorkoutTask();
                    workoutTask.execute();
                    try {
                        workoutData = workoutTask.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    HashMap<String, List<String>> children = new HashMap<String, List<String>>();
                    for(String s : workouts) {
                        children.put(s, new ArrayList<String>());
                    }
                    for(int i=0; i<workoutData.length(); i++){
                        try {

                            JSONObject works = workoutData.getJSONObject(i);
                            String name = works.getString("name");
                            JSONArray exs = works.getJSONArray("exercises");


                            for(String w : workouts) {
                                if(name.equals(w.split("\\(")[0].trim())){
                                    List<String> child = children.get(w);
                                    for (int j = 0; j < exs.length(); j++) {
                                        JSONObject row = exs.getJSONObject(j);
                                        child.add(row.getString("exercise") + ", sets: " + row.getString("sets") + ", reps: " + row.getString("reps"));
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    ExpandableListView explist = (ExpandableListView) rootView.findViewById(R.id.schedules_explist);
                    explist.setAdapter(new ExpListAdapter(activity, workouts, children));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            ScheduleTask sTask = new ScheduleTask();
            sTask.execute();
            try {
                JSONArray jsonArray = sTask.get();
                updateSpinner(jsonArray);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
    }

}
