package com.example.habittracker;

import android.content.Context;
import android.gesture.Gesture;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HabitRecyclerAdapter extends RecyclerView.Adapter<HabitRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter{
    private final static String TAG = "HabitRecyclerAdapter";

    private ArrayList<Habit> habits = new ArrayList<>();
    private ItemTouchHelper habitTouchHelper;
    private OnHabitListener onHabitListener;
    private String usernameStr;

    private FirebaseFirestore db;

    // Firebase collection constants
    private static final String COLLECTION_USERS = "users";
    private static final String COLLECTION_HABITS = "habits";

    // Habit document keys
    private static final String KEY_HABIT_TITLE = "title";
    private static final String KEY_HABIT_REASON = "reason";
    private static final String KEY_HABIT_PUBLIC_VISIBILITY = "publicVisibility";
    private static final String KEY_HABIT_HID = "hid";
    private static final String KEY_HABIT_DATE_TO_START = "dateToStart";
    private static final String KEY_HABIT_LIST_POSITION = "listPosition";
    private static final String KEY_HABIT_WEEKDAYS = "weekdays";

    public HabitRecyclerAdapter(ArrayList<Habit> habits, OnHabitListener onHabitListener, String usernameStr) {
        this.habits = habits;
        this.onHabitListener = onHabitListener;
        this.usernameStr = usernameStr;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_listview_content, parent, false);
        ViewHolder holder = new ViewHolder(view, onHabitListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.habitTitle.setText(habits.get(position).getTitle());
        holder.habitReason.setText(habits.get(position).getReason());
        holder.habitDateToStart.setText(habits.get(position).getDateToStart());
        holder.habitWeekdays.setText(habits.get(position).getWeeklySchedule().getSchedule().toString().replace("[", "").replace("]", ""));

        holder.habitReason.setVisibility(View.GONE);
        holder.habitDateToStart.setVisibility(View.GONE);
        holder.habitWeekdays.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //Visually move the habit
        Habit fromHabit = habits.get(fromPosition);
        habits.remove(fromHabit);
        habits.add(toPosition, fromHabit);
        notifyItemMoved(fromPosition, toPosition);

//        habits.get(fromPosition).setListPosition(toPosition);
//        habits.get(toPosition).setListPosition(fromPosition);

    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.habitTouchHelper = touchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnTouchListener,
            GestureDetector.OnGestureListener {

        //Setup
        TextView habitTitle;
        TextView habitReason;
        TextView habitDateToStart;
        TextView habitWeekdays;
        RelativeLayout relativeLayout;
        GestureDetector gestureDetector;
        OnHabitListener onHabitListener;

        public ViewHolder(@NonNull View itemView, OnHabitListener onHabitListener) {
            super(itemView);
            habitTitle = itemView.findViewById(R.id.habit_title);
            habitReason = itemView.findViewById(R.id.habit_reason);
            habitDateToStart = itemView.findViewById(R.id.habit_datetostart);
            habitWeekdays = itemView.findViewById(R.id.habit_weekdays);
            relativeLayout = itemView.findViewById(R.id.recycler_layout);

            this.onHabitListener = onHabitListener;

            gestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            onHabitListener.onHabitClick(getAdapterPosition(), itemView);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
            habitTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }

    public interface OnHabitListener {
        void onHabitClick(int position, View view);
    }

}
