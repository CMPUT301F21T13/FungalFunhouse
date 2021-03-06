package com.example.habittracker;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is a Fragment for the EVENTS tab
 * that uses the xml file events_fragment.xml
 * This fragment shows existing habits that when clicked
 * Send you to an Event Log of all existing days instances of that Habit
 */
public class EventsFragment extends Fragment {

    //initialize variables
    ListView eventsListView;
    private HabitListAdapter eventsListAdapter;
    private ArrayList<Habit> eventsHabitDataList;
    private UserProfile currentUser;
    private String usernameStr;

    //Firebase references
    private String COLLECTION_USERS = "users";
    private String COLLECTION_HABITS = "habits";
    private FirebaseFirestore db;
    private final String TAG = "EventsFragment";


    public EventsFragment(){
        super(R.layout.events_fragment);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.events_fragment, container, false);

        //initialize variables
        eventsListView = view.findViewById(R.id.events_fragment_listview);
        eventsHabitDataList = new ArrayList<>();

        Bundle bundle = getArguments();
        try {
            currentUser = bundle.getParcelable("user");

        } catch (NullPointerException e) {
            Log.e(TAG, "Could not get 'user' from bundle" + e);
        }
        usernameStr = currentUser.getUsername();
        loadHabitinList();

        //Sends User to show events for the clicked habit
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ShowEventsForHabitActivity.class);
                intent.putExtra("habit id", eventsHabitDataList.get(i).getHid());
                intent.putExtra("habit title", eventsHabitDataList.get(i).getTitle());
                intent.putExtra("user", usernameStr);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    /**
     * Loads all habits into eventsHabitDataList for the eventsListView
     */
    public void loadHabitinList(){
        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        eventsHabitDataList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Log.d(TAG, doc.getId() + "Loading Habits Into Listview");

                            //Grab all parameters for the habit
                            String title = (String) doc.getData().get("title");
                            String reason = (String) doc.getData().get("reason");
                            String hid = (String) doc.getData().get("hid");
                            String dateToStart = (String) doc.getData().get("dateToStart");
                            boolean publicVisibility = (boolean) doc.getData().get("publicVisibility");
                            ArrayList<String> weekdays = (ArrayList<String>) doc.getData().get("weekdays");

                            Habit habit = new Habit(title, reason, dateToStart, publicVisibility, weekdays);
                            habit.setHid(hid);
                            Log.d(TAG, habit.getTitle());

                            eventsHabitDataList.add(habit);
                        }
                        if(getActivity() != null) {
                            eventsListAdapter = new HabitListAdapter(getActivity(), eventsHabitDataList);
                            eventsListView.setAdapter(eventsListAdapter);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
