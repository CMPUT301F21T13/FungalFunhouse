package com.example.habittracker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    public FloatingActionButton addHabit;
    public FloatingActionButton editHabit;
    public FloatingActionButton deleteHabit;
    public UserProfile currentUser;
    public int selectedHabit;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Hardcoded data for testing habit tab
        currentUser = new UserProfile("user1");
        Habit testHabit = new Habit();
        testHabit.setTitle("Water Plants");
        testHabit.setReason("So they don't die");
        testHabit.weeklySchedule.addMonday();
        testHabit.weeklySchedule.addWednesday();
        testHabit.weeklySchedule.addFriday();
        currentUser.addHabit(testHabit);

        View view = inflater.inflate(R.layout.habit_fragment, container, false);

        if(currentUser.getHabitList() != null){
            habitListView = view.findViewById(R.id.habit_listview);
            Context context = getContext();
            habitListAdapter = new HabitListAdapter(context, currentUser.getHabitList());
            habitListView.setAdapter(habitListAdapter);
        }

        //habitListView listener
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedHabit = i;// i = position of the current habit selected
            }
        });

        //Add habit button listener
        addHabit = view.findViewById(R.id.add_habbit_floating_button);
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO(GLENN): add functionality
            }
        });

        //Edit habit button listener
        editHabit = view.findViewById(R.id.edit_habit_floating_button);
        editHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO(GLENN): add functionality
            }
        });

        //Delete habit button listener
        deleteHabit = view.findViewById(R.id.delete_habit_floating_button);
        deleteHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO(GLENN): add functionality
            }
        });

        return view;

    }




}
