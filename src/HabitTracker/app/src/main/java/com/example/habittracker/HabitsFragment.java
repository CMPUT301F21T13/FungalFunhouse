package com.example.habittracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * This is a Fragment for the HABITS tab
 * that uses the xml file habit_fragment.xml
 */
public class HabitsFragment extends Fragment {
    public HabitsFragment() {
        super(R.layout.habit_fragment);
    }
    /*TODO(GLENN): Create logic for Habits fragment including: selecting a habit, add button,
       edit button, delete button, and visual indicator.

       After this move onto the activities for add habit and edit habit (should be able to reuse
       the same activity just with a different data Bundle.
     */

    //Declare variables
    public HabitListAdapter habitListAdapter;
    public ListView habitListView;
    public Button addHabit;
    public Button editHabit;
    public Button deleteHabit;

    //Hardcoded variables until FireStore DB gets implemented
    public HabitList habitList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


    }




}
