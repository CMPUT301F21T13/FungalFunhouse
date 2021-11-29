package com.example.habittracker;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HabitRecyclerAdapter extends RecyclerView.Adapter<HabitRecyclerAdapter.ViewHolder> implements ItemTouchHelperAdapter{
    private final static String TAG = "HabitRecyclerAdapter";

    private ArrayList<Habit> habits;
    private ItemTouchHelper habitTouchHelper;
    private OnHabitListener onHabitListener;


    public HabitRecyclerAdapter(ArrayList<Habit> habits, OnHabitListener onHabitListener) {
        this.habits = habits;
        this.onHabitListener = onHabitListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_listview_content, parent, false);
        ViewHolder holder = new ViewHolder(view, onHabitListener);
        return holder;
    }

    /**
     * Sets the specific habits details to the views being displayed
     * @param holder The current holder that needs to get information
     * @param position The position of the habit in the array and recyclerview
     */
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

    /**
     * Swaps the positions of the habits to mimic the positions displayed to the user
     * after the user has manually reordered them on screen
     * @param fromPosition Old position of the habit dragged
     * @param toPosition New position of the habit dragged
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //Visually move the habit
        Habit fromHabit = habits.get(fromPosition);
        habits.remove(fromHabit);
        habits.add(toPosition, fromHabit);
        notifyItemMoved(fromPosition, toPosition);

    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.habitTouchHelper = touchHelper;
    }

    /**
     * Used to control logic and views for each individual view in RecyclerView
     */
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

        /**
         * Initializes the views, layout, Detector and listener to their proper IDs
         * @param itemView
         * @param onHabitListener
         */
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

        /**
         * Acts as an onClickEventListener for recyclerview
         * When the view is clicked on it calls onHabitClick to do further logic
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            onHabitListener.onHabitClick(getAdapterPosition(), itemView);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        /**
         * When a view is pressed for longer than a click it initiates startDrag on that view
         * @param motionEvent
         */
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
