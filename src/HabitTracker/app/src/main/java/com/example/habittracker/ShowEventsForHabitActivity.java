package com.example.habittracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
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
    private String habitTitle;
    private HabitEvent currentEvent;

    private FirebaseFirestore db;
    private SimpleDateFormat sdf;
    private Calendar calendar;
    private ActivityResultLauncher<Intent> activityLauncher;

    private FloatingActionButton showEventsAddButton;
    private FloatingActionButton showEventsEditButton;
    private FloatingActionButton showEventsDeleteButton;
    private TextView showEventsLogView;

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
        showEventsLogView = findViewById(R.id.show_events_log_id);



        db = FirebaseFirestore.getInstance();
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        showEventsDataList = new ArrayList<>();

        try{
            habitHid = getIntent().getStringExtra("habit id");
            usernameStr = getIntent().getStringExtra("user");
            habitTitle = getIntent().getStringExtra("habit title");
            showEventsLogView.setText(habitTitle);

        } catch (NullPointerException e){
            Log.e("AddEventActivity: ", "Could not get 'habit id', 'user' or 'date' from bundle" + e);
        }

        loadHabitEventList();


        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                }
            }
        });

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

                Intent intent = new Intent(ShowEventsForHabitActivity.this, AddEventActivity.class);
                intent.putExtra("habit id", habitHid);
                intent.putExtra("user", usernameStr);
                intent.putExtra("Flag", "AddEvent");
                activityLauncher.launch(intent);
            }
        });

        showEventsDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentEvent != null){
                    deleteHabitEvent(currentEvent);
                }
            }
        });

        showEventsEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentEvent != null){
                    //TODO Send HabitEvent to "AddEventActivity" with activity Launcher for result
                    //TODO Set "Edit" Flag on Intent when sending to AddEventActivity
                    Intent intent = new Intent(ShowEventsForHabitActivity.this, AddEventActivity.class);
                    intent.putExtra("habit id", habitHid);
                    intent.putExtra("user", usernameStr);
                    intent.putExtra("date", sdf.format(currentEvent.getDate().getTime()));
                    intent.putExtra("Flag", "EditEvent");
                    activityLauncher.launch(intent);
                }
            }
        });


    }

    public void deleteHabitEvent(HabitEvent event){
        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS).document(habitHid)
                .collection(COLLECTION_EVENTS).document(sdf.format(event.getDate().getTime()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(@NonNull Void unused) {
                        Log.d(TAG, "Successful in deleting document");
                        loadHabitEventList();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to delete document");
            }
        });
    }

    /**
     * Loads in every HabitEvent for this Activities Habit
     * Into showEventsDataList
     * Mainly used for a HabitEvent ListView
     */
    public void loadHabitEventList(){
        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS)
                .document(habitHid).collection(COLLECTION_EVENTS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                        showEventsDataList.clear();
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