package com.example.habittracker;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This is a Fragment for the DAILY tab
 * that uses the xml file daily_fragment.xml
 */
public class DailyFragment extends Fragment {
    public DailyFragment(){
        super(R.layout.daily_fragment);
    }

    //declare variables
    ListView dailyListView;
    private DailyHabitListAdapter dailyListAdapter;
    private ArrayList<Habit> dailyDataList;
    private UserProfile currentUser;
    private String usernameStr;
    private Calendar calendar;
    private Date dateToCheck;
    private Habit currentHabit;

    private FirebaseFirestore db;

    private String COLLECTION_USERS = "users";
    private String COLLECTION_HABITS = "habits";
    private String COLLECTION_HABIT_EVENTS = "habitEvents";
    private String KEY_LOCATION = "location";
    private String KEY_IMAGE = "image";
    private String KEY_COMMENT = "comment";

    private SimpleDateFormat sdf;
    private static final String TAG = "DailyFragment";
    private ActivityResultLauncher<Intent> activityLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.daily_fragment, container, false);

        //initialize variables
        dailyListView = view.findViewById(R.id.daily_listview);

        calendar = Calendar.getInstance();
        dateToCheck = calendar.getTime();

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Grab the username of the current logged in user
        Bundle bundle = getArguments();
        try {
            currentUser = bundle.getParcelable("user");
        } catch (NullPointerException e) {
            Log.e(TAG, "Could not get 'user' from bundle" + e);
        }


        usernameStr = currentUser.getUsername();
        dailyDataList = new ArrayList<>();

        // Grab all of the habits from the database and fill the ListView
        loadHabitList();

        activityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    loadHabitList();

                }
            }
        });
            dailyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    currentHabit = dailyDataList.get(i);
                    if(currentHabit.getHabitEventList()== null) {
                        Intent intent = new Intent(getActivity(), AddEventActivity.class);
                        intent.putExtra("habit id", currentHabit.getHid());
                        intent.putExtra("user", usernameStr);
                        intent.putExtra("date", sdf.format(dateToCheck));
                        getActivity().startActivity(intent);
                    }else if(!currentHabit.getHabitEventList().get(0).getDone()) {
                        Log.d(TAG, "Done " + currentHabit.getHabitEventList().get(0).getDone());
                            Intent intent = new Intent(getActivity(), AddEventActivity.class);
                            intent.putExtra("habit id", currentHabit.getHid());
                            intent.putExtra("user", usernameStr);
                            intent.putExtra("date", sdf.format(dateToCheck));
                        activityLauncher.launch(intent);
                        }

                }
            });

        return view; }


        public void loadHabitList(){
        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        dailyDataList.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            Log.d(TAG, doc.getId());
                            String title = (String) doc.getData().get("title");
                            String reason = (String) doc.getData().get("reason");
                            String hid = (String) doc.getData().get("hid");
                            String dateToStart = (String) doc.getData().get("dateToStart");
                            boolean publicVisibility = (boolean) doc.getData().get("publicVisibility");
                            ArrayList<String> weekdays = (ArrayList<String>) doc.getData().get("weekdays");

                            //TODO(GLENN): update this to include visual indicator code
                            Habit habit = new Habit(title, reason, hid, dateToStart, publicVisibility, weekdays, 0);

                            if(habitHappensToday(habit, dateToCheck)) {
                                dailyDataList.add(habit);
                            }


                            Context context = getContext();
                            dailyListAdapter = new DailyHabitListAdapter(context, dailyDataList, calendar, usernameStr);
                            dailyListView.setAdapter(dailyListAdapter);
                        }
                    }
                });
        }
    /**
     * Tests if the habit inputted occurs during the
     * inputted date
     * @param habit : the habit we are checking
     * @param currentDate : the day we are checking for
     * @return true if the habit happened at this date, false if not
     */
    public boolean habitHappensToday(Habit habit, Date currentDate){
        //Initialize Variables
        String currentDayOfWeek;
        Date habitStartDate;

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        currentDayOfWeek = simpleDateformat.format(currentDate);

        //Grab startDate from habit
        try {
            habitStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(habit.getDateToStart());
        } catch (ParseException e) {
            Log.d(TAG, "Start-Date failed to parse: " + habit.getDateToStart());
            return false;
        }

        //Grab current Date's day of the week position

        //Test the two dates against each other
        if(currentDate.before(habitStartDate)){
            return false;
        }else{
            //check if the weekdays match up;
            ArrayList<String> habitWeeklySchedule = habit.getWeeklySchedule().getSchedule();
            for (String habitDayOfWeek : habitWeeklySchedule){
                if(currentDayOfWeek.equals(habitDayOfWeek)){
                    return true;
                }
            }
        }

        return false;
        }





}
