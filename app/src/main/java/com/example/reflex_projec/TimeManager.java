package com.example.reflex_projec;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class TimeManager {

    public Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }


}
