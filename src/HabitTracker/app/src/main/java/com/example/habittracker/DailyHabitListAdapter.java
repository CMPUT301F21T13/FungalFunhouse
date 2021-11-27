package com.example.habittracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DailyHabitListAdapter extends ArrayAdapter<Habit> {


    private ArrayList<Habit> habits;
    private Context context;
    private Calendar calendar;
    private FirebaseFirestore db;
    private String usernameStr;

    private String COLLECTION_USERS = "users";
    private String COLLECTION_HABITS = "habits";
    private String COLLECTION_EVENTS = "habitEvents";

    private String KEY_DONE = "done";
    private String KEY_LOCATION = "location";
    private String KEY_IMAGE = "image";
    private String KEY_COMMENT = "comment";

    private SimpleDateFormat sdf;
    private static final String TAG = "DAILYADAPTER";

    public DailyHabitListAdapter(Context context, ArrayList<Habit> habits, Calendar calendar, String username) {
        super(context, 0, habits);
        this.context = context;
        this.habits = habits;
        this.calendar = calendar;
        this.usernameStr = username;
    }

    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        db = FirebaseFirestore.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.daily_listview_content, parent, false);
        }

        Habit habit = habits.get(position);

        //Setup required Views (placeholders for photo and location)
        TextView dailyTitleTextView = (TextView) view.findViewById(R.id.daily_listview_title);
        ImageView dailyPhotoImageView = (ImageView) view.findViewById(R.id.daily_listview_photograph);
        TextView dailyLocationTextView = (TextView) view.findViewById(R.id.daily_listview_location);
        TextView dailyCommentTextView = (TextView) view.findViewById(R.id.daily_listview_comment);

        //set up Layouts
        LinearLayout locationLayout = view.findViewById(R.id.daily_listview_location_box);
        LinearLayout contentLayout = view.findViewById(R.id.daily_listview_layout);

        //Deal with any Done or not Done Habits
        dailyCommentTextView.setVisibility(View.GONE);
        dailyPhotoImageView.setVisibility(View.GONE);
        locationLayout.setVisibility(View.GONE);

        Log.d(TAG, "Habit: " + habit.getHid());
        Log.d(TAG, "Habit Events " + habit.getHabitEventList());

        db.collection(COLLECTION_USERS).document(usernameStr).collection(COLLECTION_HABITS)
                .document(habit.getHid()).collection(COLLECTION_EVENTS)
                .document(sdf.format(calendar.getTime())).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "Document Retrieval (Habit Events) was successful");

                            //grab attributes and set HabitEvent
                            HabitEvent habitEvent = new HabitEvent(calendar);
                            if (documentSnapshot.getData().get(KEY_IMAGE) != null) {
                                Bitmap dailyImage =StringToBitMap(documentSnapshot.getData().get(KEY_IMAGE).toString());
                                habitEvent.setPhotograph(dailyImage);
                                dailyPhotoImageView.setImageBitmap(habitEvent.getPhotograph());
                                dailyPhotoImageView.setVisibility(View.VISIBLE);
                            }
                            String dailyComment = (String) documentSnapshot.getData().get(KEY_COMMENT);
                            boolean dailyDone = (boolean) documentSnapshot.getData().get(KEY_DONE);

                            //add into habitevent
                            habitEvent.setComment(dailyComment);

                            habitEvent.setDone(dailyDone);

                            ArrayList<HabitEvent> events = new ArrayList<>();
                            events.add(habitEvent);
                            habit.setHabitEventList(events);

                            Log.d(TAG, "bitmap: " + habitEvent.getPhotograph());
                            //Set Listview Item
                            dailyCommentTextView.setText("'" + dailyComment+ "'");
                            dailyCommentTextView.setVisibility(View.VISIBLE);
                            dailyTitleTextView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.tick_mark_checked,0);

                            Log.d(TAG, "Habit Event Comment" + dailyComment);
                        } else {
                            Log.d(TAG, "Document Retrieval (Habit Events) was empty");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Document Retrieval (Habit Events) failed");
            }
        });
        dailyTitleTextView.setText(habit.getTitle());

        //add in holders for all habit events variables afterwards


        return view;

    }
    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

}
