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
    private boolean following;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Grab the username of the current logged in user
        Bundle bundle = getArguments();
        try {
            usernameStr = bundle.get("user").toString();
        } catch (NullPointerException e) {
            Log.e("HabitsFragment: ", "Could not get 'user' from bundle" + e);
        }

        try {
            following = bundle.getBoolean("following");
        }catch(NullPointerException e){
            Log.e("HabitsFragment: ", "could not get 'following' from bundle" + e);
        }

        currentUser = bundle.getParcelable("user");
        View view = inflater.inflate(R.layout.habit_fragment, container, false);


        if(currentUser.getHabitList() != null){
            habitListView = view.findViewById(R.id.habit_listview);
            Context context = getContext();
            habitListAdapter = new HabitListAdapter(context, currentUser.getHabitList());
            habitListView.setAdapter(habitListAdapter);
        }

        //Initialize variables
        addHabit = view.findViewById(R.id.add_habbit_floating_button);
        editHabit = view.findViewById(R.id.edit_habit_floating_button);
        deleteHabit = view.findViewById(R.id.delete_habit_floating_button);
        editHabit.setVisibility(View.GONE);//Gone until an item on the list is selected
        deleteHabit.setVisibility(View.GONE);//Gone until an item on the list is selected

        //Sets the buttons to not display if the Fragment is currently in following view
        if(following) {
            addHabit.setVisibility(View.INVISIBLE);
            editHabit.setVisibility(View.INVISIBLE);
            deleteHabit.setVisibility(View.INVISIBLE);
        }
        //habitListView listener
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO(GLENN): Add highlight functionality to the selected item
                if(!following) {
                    editHabit.setVisibility(View.VISIBLE);
                    deleteHabit.setVisibility(View.VISIBLE);
                }
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
                habitListAdapter.remove(habitListAdapter.getItem(selectedHabit));
                habitListAdapter.notifyDataSetChanged();
            }
        });

        return view;

    }




}
