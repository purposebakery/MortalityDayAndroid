package de.techlung.android.mortalityday.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.techlung.android.mortalityday.enums.Frequency;
import de.techlung.android.mortalityday.settings.Preferences;

public final class MortalityDayUtil {

    private MortalityDayUtil() {

    }
    public static boolean isMortalityDay(){
        Calendar day = new GregorianCalendar();
        day.setTime(new Date());
        return isMortalityDay(day);
    }

    public static boolean isMortalityDay(Calendar day) {
        int weekDay = day.get(Calendar.DAY_OF_WEEK);

        if (Preferences.getFrequency() == Frequency.ONCE_A_WEEK) {
            if (weekDay == Preferences.getDay1()) {
                return true;
            }
        } else {
            if (weekDay == Preferences.getDay1() || weekDay == Preferences.getDay2()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get next mortality day. skip current day if today is a mortality day.
     * @return
     */
    public static Calendar getNextMortalityDay() {
        Calendar day = new GregorianCalendar();
        day.setTime(new Date());

        day.add(Calendar.MINUTE, 1440); // add one Day

        while (!isMortalityDay(day)) {
            day.add(Calendar.MINUTE, 1440); // add one Day
        }

        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE,0);
        day.set(Calendar.SECOND,0);
        day.set(Calendar.MILLISECOND, 0);
        return day;
    }

    public static String getDateFormatted(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(date);
    }

}
