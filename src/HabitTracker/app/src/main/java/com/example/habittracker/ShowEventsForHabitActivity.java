package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShowEventsForHabitActivity extends AppCompatActivity {
    private ListView showEventsListView;
    private EventsListAdapter showEventsListAdaptor;
    private ArrayList<HabitEvent> showEventsDataList;
    private String usernameStr;
    private String habitHid;
    private HabitEvent currentEvent;

    private FirebaseFirestore db;
    private SimpleDateFormat sdf;
    private Calendar calendar;

    private FloatingActionButton showEventsAddButton;
    private FloatingActionButton showEventsEditButton;
    private FloatingActionButton showEventsDeleteButton;

    private String COLLECTION_USERS = "users";
    private String COLLECTION_HABITS = "habits";
    private String COLLECTION_EVENTS = "habitEvents";
    private final String TAG = "ShowEventsForActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_events_for_habit);

        //initialize variables
        showEventsListView = findViewById(R.id.show_events_listview);
        showEventsAddButton = findViewById(R.id.show_events_add_floating_button);
        showEventsEditButton = findViewById(R.id.show_events_edit_floating_button);
        showEventsDeleteButton = findViewById(R.id.show_events_delete_floating_button);

        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        showEventsDataList = new ArrayList<>();

        try{
            habitHid = getIntent().getStringExtra("habit id");
            usernameStr = getIntent().getStringExtra("user");

        } catch (NullPointerException e){
            Log.e("AddEventActivity: ", "Could not get 'habit id', 'user' or 'date' from bundle" + e);
        }

        loadHabitEventList();

        //ListView gives selected habitEvent
        showEventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentEvent = showEventsDataList.get(i);
            }
        });

        showEventsAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        showEventsDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentEvent != null){
                    //TODO Remove habitEvent from Database
                }
            }
        });

        showEventsEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentEvent != null){
                    //TODO Send HabitEvent to "AddEventActivity" with activity Launcher for result
                    //and set flags to ask for the date too
                    //also modify the addevent activity to show incoming elements (only does comments for now) 
                }
            }
        });


    }

    public void loadHabitEventList(){
        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS)
                .document(habitHid).collection(COLLECTION_EVENTS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                                HabitEvent event = new HabitEvent();
                                Date date;
                                try {
                                    date = sdf.parse((String) doc.getData().get("dateTime"));
                                    calendar = Calendar.getInstance();
                                    calendar.setTime(date);
                                    event.setDate(calendar);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                showEventsDataList.add(event);
                            }
                            showEventsListAdaptor = new EventsListAdapter(ShowEventsForHabitActivity.this, showEventsDataList, usernameStr);
                            showEventsListView.setAdapter(showEventsListAdaptor);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Document Retrieval for Events Failed");
            }
        });
    }
}