package com.example.habittracker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.google.android.gms.maps.model.LatLng;

import java.util.Calendar;

public class HabitEventUnitTest {

    public HabitEvent mockEvent(){
        HabitEvent event = new HabitEvent();
        return event;
    }

    @Test public void testSettersGetters(){
        HabitEvent mockEvent = mockEvent();

        mockEvent.setDone(true);
        assertEquals(true, mockEvent.getDone());

        mockEvent.setComment("mock comment");
        assertEquals("mock comment", mockEvent.getComment());

        LatLng position = new LatLng(20, 20);
        mockEvent.setLocation(position);
        assertEquals(position, mockEvent.getLocation());

        Calendar calendar = Calendar.getInstance();
        mockEvent.setDate(calendar);
        assertEquals(calendar, mockEvent.getDate());
    }
}
