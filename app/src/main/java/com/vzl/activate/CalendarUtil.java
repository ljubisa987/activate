package com.vzl.activate;

import java.util.Calendar;

public class CalendarUtil {

    public static Calendar getFirstOfTheMonth(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar;
    }

}
