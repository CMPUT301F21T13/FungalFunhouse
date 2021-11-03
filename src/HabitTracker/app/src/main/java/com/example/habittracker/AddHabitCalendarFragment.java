package com.example.habittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddHabitCalendarFragment extends DialogFragment {

    private String dateToStart;

    private CalendarView dateStartedCalendarView;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onConfirmPressed(String dateToStart);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_habit_calendar_fragment, null);

        //Grab bundle to see if editing or adding a new habit
        final Bundle bundle = this.getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //set views
        dateStartedCalendarView = view.findViewById(R.id.habit_calendar_calendarView);

        //Event handler for CalendarView
        dateStartedCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                month++; //months range from 0-11
                if (month < 10) {
                    dateToStart = year + "-0" + month + "-" + dayOfMonth;
                } else {
                    dateToStart = year + "-" + month + "-" + dayOfMonth;
                }
            }
        });

        if(bundle == null) {
        //If the user doesn't interact with the calendar then the date will be set to the current day
        dateToStart = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        builder.setView(view)
                .setTitle("Pick Date To Start")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onConfirmPressed(dateToStart);
                    }
                });
        }
        else { //data in bundle, therefore editing
            //set CalendarView to habits start date
            dateToStart = bundle.get("dateToStart").toString();
            try {
                dateStartedCalendarView.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(dateToStart).getTime(), true, true);
            }
            catch (ParseException e) {
                e.printStackTrace();;
            }

            builder.setView(view)
                    .setTitle("Edit Start Date")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listener.onConfirmPressed(dateToStart);
                        }
                    });

        }

        return builder.create();
    }

}
