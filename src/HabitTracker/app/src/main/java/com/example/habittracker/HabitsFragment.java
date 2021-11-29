package com.example.habittracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is a Fragment for the HABITS tab that uses the xml file
 * habit_fragment.xml
 */
public class HabitsFragment extends Fragment  implements HabitRecyclerAdapter.OnHabitListener {
    public HabitsFragment() {
        super(R.layout.habit_fragment);
    }
    // TODO(GLENN): Add visual indicator of how well the user is following the
    // habits

    // Declare variables
    private FloatingActionButton addHabit;
    private FloatingActionButton editHabit;
    private FloatingActionButton deleteHabit;
    private UserProfile currentUser;
    private int selectedHabit;
    private View selectedView;
    private String usernameStr;
    private ArrayList<Habit> habitArrayList;
    private boolean following;
    private FirebaseFirestore db;

    //Testing recyclerview
    private RecyclerView habitRecyclerView;
    private HabitRecyclerAdapter habitRecyclerAdapter;

    // Constants
    private static final String TAG = "HabitsFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.habit_fragment, container, false);

        // Initialize variables
        //habitListView = view.findViewById(R.id.habit_listview);
        addHabit = view.findViewById(R.id.add_habbit_floating_button);
        editHabit = view.findViewById(R.id.edit_habit_floating_button);
        deleteHabit = view.findViewById(R.id.delete_habit_floating_button);
        editHabit.setVisibility(View.GONE);// Gone until an item on the list is selected
        deleteHabit.setVisibility(View.GONE);// Gone until an item on the list is selected
        selectedHabit = -1;

        //Testing recyclerView
        habitRecyclerView = view.findViewById(R.id.habit_listview);
        // adds vertical dividers for items in recyclerview
        habitRecyclerView.addItemDecoration(new DividerItemDecoration(habitRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // Grab the username of the current logged in user
        Bundle bundle = getArguments();
        try {
            currentUser = bundle.getParcelable("user");
        } catch (NullPointerException e) {
            Log.e(TAG, "Could not get 'user' from bundle" + e);
        }


        try {
            following = bundle.getBoolean("following");
        } catch (NullPointerException e) {
            Log.e(TAG, "could not get 'following' from bundle" + e);
        }

        usernameStr = currentUser.getUsername();
        habitArrayList = new ArrayList<>();

        try{
            Log.d(TAG, usernameStr);
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: usernameStr is null", e);
        }

        // Grab all of the habits from the database and fill the Recyclerview
        // Use a snapshot listener so whenever the database is updated so is the app

        //If implementing listPositions use .orderBy("listPosition", Query.Direction.ASCENDING)
        db.collection("users").document(usernameStr).collection("habits")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        habitArrayList.clear();
                        for (QueryDocumentSnapshot doc : value) {

                            Log.d(TAG, (String) doc.getData().get("title"));
                            String title = (String) doc.getData().get("title");
                            String reason = (String) doc.getData().get("reason");
                            String hid = (String) doc.getData().get("hid");
                            String dateToStart = (String) doc.getData().get("dateToStart");
                            //long listPosition = (long) doc.getData().get("listPosition");
                            boolean publicVisibility = (boolean) doc.getData().get("publicVisibility");
                            ArrayList<String> weekdays = (ArrayList<String>) doc.getData().get("weekdays");

                            //TODO(GLENN): Update this to include visual indicator code
                            Habit habit = new Habit(title, reason, hid, dateToStart, publicVisibility, weekdays, 0);
                            Log.d(TAG, habit.toString());

                            habitArrayList.add(habit);

                            //For viewing following habits
                            //If the habit is private remove it from the list
                            if(following && !habit.getPublicVisibility()){
                                habitArrayList.remove(habit);
                            }

                            Context context = getContext();

                            habitRecyclerAdapter = new HabitRecyclerAdapter(habitArrayList, HabitsFragment.this);
                            habitRecyclerView.setAdapter(habitRecyclerAdapter);
                            habitRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                            ItemTouchHelper.Callback callback = new HabitItemTouchHelper(habitRecyclerAdapter);
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
                            itemTouchHelper.attachToRecyclerView(habitRecyclerView);
                            habitRecyclerAdapter.setTouchHelper(itemTouchHelper);

                        }

                    }
                });


        // Sets the buttons to not display if the Fragment is currently in following
        // view
        if (following) {
            addHabit.setVisibility(View.INVISIBLE);
            editHabit.setVisibility(View.INVISIBLE);
            deleteHabit.setVisibility(View.INVISIBLE);
        }

        // Add habit button listener
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddHabitActivity.class);
                intent.putExtra("user", usernameStr);
                intent.putExtra("list_size", habitArrayList.size());
                startActivity(intent);
            }
        });

        // Edit habit button listener
        editHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddHabitActivity.class);
                // Need to send editing boolean and habit title to the activity
                intent.putExtra("user", usernameStr);
                intent.putExtra("editing", true);
                intent.putExtra("habitHID", habitArrayList.get(selectedHabit).getHid());
                startActivity(intent);
            }
        });

        // Delete habit button listener
        deleteHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //This commented chunk deletes the habitEvent collection from a Habit document
                //Causes the app to crash from a bug in AddEventActivity somehow

//                //Delete the habitEvents first then the Habit itself
//                try{
//                    //Grab all the habitEvent documents
//                    db.collection("users").document(usernameStr).collection("habits")
//                            .document(habitListAdapter.getItem(selectedHabit).getHid()).collection("habitEvents")
//                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    for (QueryDocumentSnapshot doc : value) {
//                                        //For each habitEvent document, delete it
//                                        db.collection("users").document(usernameStr).collection("habits")
//                                                .document(doc.getData().get("dateTime").toString()).delete();
//                                    }
//                                }
//                            });
//                }catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }

                try{
                db.collection("users").document(usernameStr).collection("habits")
                        .document(habitArrayList.get(selectedHabit).getHid()).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "Habit deleted");
                                Toast.makeText(getContext(), "Habit Deleted", Toast.LENGTH_LONG).show();

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, e.toString());
                                Toast.makeText(getContext(), "Database Error", Toast.LENGTH_LONG).show();
                            }
                        });
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    Toast.makeText(getContext(), "Database Error, try again",
                            Toast.LENGTH_LONG).show();
                }


            }
        });

        return view;

    }

    @Override
    public void onHabitClick(int position, View view) {
        //May need to keep track of the old view by setting it to a variable
            if(selectedView != null) {
                selectedView.findViewById(R.id.habit_reason).setVisibility(View.GONE);
                selectedView.findViewById(R.id.habit_datetostart).setVisibility(View.GONE);
                selectedView.findViewById(R.id.habit_weekdays).setVisibility(View.GONE);
                selectedView.findViewById(R.id.habit_indicator_green).setVisibility(View.GONE);
                selectedView.findViewById(R.id.habit_indicator_yellow).setVisibility(View.GONE);
                selectedView.findViewById(R.id.habit_indicator_red).setVisibility(View.GONE);
                selectedView.findViewById(R.id.habit_checkmark).setVisibility(View.GONE);
            }

            if (!following) {
                editHabit.setVisibility(View.VISIBLE);
                deleteHabit.setVisibility(View.VISIBLE);
            }
            selectedHabit = position;
            selectedView = view;
            view.findViewById(R.id.habit_reason).setVisibility(View.VISIBLE);
            view.findViewById(R.id.habit_datetostart).setVisibility(View.VISIBLE);
            view.findViewById(R.id.habit_weekdays).setVisibility(View.VISIBLE);

            db.collection("users").document(usernameStr).collection("habits")
                    .document(habitArrayList.get(position).getHid()).collection("habitEvents").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                            int eventsCompleted = 0;
                            for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                boolean doneFlag = (boolean) doc.getData().get("done");
                                if(doneFlag) {
                                    Log.d(TAG, "onEvent: Got event complete");
                                    eventsCompleted++;
                                }
                            }
                            habitArrayList.get(position).setEventsCompleted(eventsCompleted);


                            //Deal with visual indicator
                            double ratio = habitArrayList.get(position).getCompletionRatio();

                            Log.d(TAG, "onHabitClick: eventsCompleted = " + habitArrayList.get(position).getEventsCompleted());
                            Log.d(TAG, "onHabitClick: ratio = " + ratio);

                            if(habitArrayList.get(position).getEventsCompleted() == 0 && habitArrayList.get(position).getPastEventDays() == 0.0){
                                selectedView.findViewById(R.id.habit_indicator_green).setVisibility(View.GONE);
                                selectedView.findViewById(R.id.habit_indicator_yellow).setVisibility(View.GONE);
                                selectedView.findViewById(R.id.habit_indicator_red).setVisibility(View.GONE);
                                selectedView.findViewById(R.id.habit_checkmark).setVisibility(View.GONE);
                            }
                            else if(ratio <= 0.33){
                                view.findViewById(R.id.habit_indicator_red).setVisibility(View.VISIBLE);
                            }
                            else if(ratio > 0.33 && ratio <= 0.66){
                                view.findViewById(R.id.habit_indicator_yellow).setVisibility(View.VISIBLE);
                            }
                            else if(ratio > 0.66 && ratio <= 0.99){
                                view.findViewById(R.id.habit_indicator_green).setVisibility(View.VISIBLE);
                            }
                            else {
                                view.findViewById(R.id.habit_checkmark).setVisibility(View.VISIBLE);
                            }

                        }
                    });

        }
}
