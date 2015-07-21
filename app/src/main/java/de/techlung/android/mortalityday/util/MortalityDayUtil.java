package de.techlung.android.mortalityday.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.techlung.android.mortalityday.enums.Frequency;
import de.techlung.android.mortalityday.enums.WeekDay;
import de.techlung.android.mortalityday.settings.Preferences;

public final class MortalityDayUtil {

    private MortalityDayUtil() {

    }

    public static boolean isMortalityDay() {
        WeekDay weekDay = getWeekDay();

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

    public static WeekDay getWeekDay() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        WeekDay weekDay = WeekDay.getWeekdayFromCalendarWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        return weekDay;
    }

    public static WeekDay getNextMortalityDay() {
        List<WeekDay> mortalityDays = new ArrayList<WeekDay>();
        mortalityDays.add(Preferences.getDay1());
        if (Preferences.getFrequency() == Frequency.TWICE_A_WEEK) {
            mortalityDays.add(Preferences.getDay2());
        }

        WeekDay currentWeekDay = getWeekDay();
        // TODO get next

        return currentWeekDay;
    }
}
