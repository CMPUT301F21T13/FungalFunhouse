package com.example.habittracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This is a Fragment for the HABITS tab
 * that uses the xml file habit_fragment.xml
 */
public class HabitsFragment extends Fragment {
    public HabitsFragment() {
        super(R.layout.habit_fragment);
    }
    //TODO(GLENN): Add visual indicator of how well the user is following the habits

    //Declare variables
    private HabitListAdapter habitListAdapter;
    private ListView habitListView;
    private FloatingActionButton addHabit;
    private FloatingActionButton editHabit;
    private FloatingActionButton deleteHabit;
    private UserProfile currentUser;
    private int selectedHabit;
    private String usernameStr;
    private ArrayList<Habit> habitArrayList;

    //Constants
    private static final String TAG = "HabitsFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habit_fragment, container, false);

        //Initialize variables
        habitListView = view.findViewById(R.id.habit_listview);
        addHabit = view.findViewById(R.id.add_habbit_floating_button);
        editHabit = view.findViewById(R.id.edit_habit_floating_button);
        deleteHabit = view.findViewById(R.id.delete_habit_floating_button);
        editHabit.setVisibility(View.GONE);//Gone until an item on the list is selected
        deleteHabit.setVisibility(View.GONE);//Gone until an item on the list is selected

        //Grab the username of the current logged in user
        Bundle bundle = getArguments();
        try {
            usernameStr = bundle.get("user").toString();
        } catch (NullPointerException e) {
            Log.e("HabitsFragment: ", "Could not get 'user' from bundle" + e);
        }

        //Hardcoded data for testing habit tab
        currentUser = new UserProfile("user1");
        Habit testHabit = new Habit();
        testHabit.setTitle("Water Plants");
        testHabit.setReason("So they don't die");
        testHabit.weeklySchedule.addMonday();
        testHabit.weeklySchedule.addWednesday();
        testHabit.weeklySchedule.addFriday();
        currentUser.addHabit(testHabit);


        //Hardcode current user for testing
        usernameStr = "mockUser";


        habitArrayList = new ArrayList<>();
        Context context = getContext();
        habitListAdapter = new HabitListAdapter(context, habitArrayList);
        habitListView.setAdapter(habitListAdapter);


        final CollectionReference habitCollection = Serialization.getHabitCollection(usernameStr);
        habitCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    Log.d(TAG, doc.getId());
                    /*
                    String title = doc.getString("title");
                    String reason = doc.getString("reason");
                    String dateToStart = doc.getString("dateToStart");
                    UUID hid = (UUID) doc.getData().get("hid");
                    boolean publicVisibility = doc.getBoolean("publicVisibility");
                    ArrayList<String> weeklySchedule = (ArrayList<String>) doc.getData().get("weekdays");
                     */

                    Habit habit = Serialization.getHabit(usernameStr, doc.getId());
                    Log.d(TAG, habit.toString());
                    habitArrayList.add(habit); //Adding the habits from firestore
                }
                habitListAdapter.notifyDataSetChanged();// Notifying the adapter to render new data
            }
        });





        //habitListView listener
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO(GLENN): Add highlight functionality to the selected item
                editHabit.setVisibility(View.VISIBLE);
                deleteHabit.setVisibility(View.VISIBLE);
                selectedHabit = i;// i = position of the current habit selected
            }
        });

        //Add habit button listener
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddHabitActivity.class);
                intent.putExtra("user", usernameStr);
                startActivity(intent);
            }
        });

        //Edit habit button listener
        editHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddHabitActivity.class);
                //Need to send editing boolean and habit title to the activity
                intent.putExtra("user", usernameStr);
                intent.putExtra("editing", true);

                //Change this to hid
                intent.putExtra("habitTitle", habitListAdapter.getItem(selectedHabit).getTitle());
                startActivity(intent);
            }
        });

        //Delete habit button listener
        deleteHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO(GLENN): when highlight functionality is added, will need to remove ghost highlight after deleting a habit
                //TODO(GLENN): Need to remove the habit from the database

                /*
                Serialization.deleteHabit(usernameStr, habitListAdapter.getItem(selectedHabit));

                final CollectionReference habitCollection = Serialization.getHabitCollection();

                 */



                habitListAdapter.notifyDataSetChanged();
            }
        });

        return view;

    }




}
