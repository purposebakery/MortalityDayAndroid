package de.techlung.android.mortalityday.enums;

import java.util.Calendar;

public enum WeekDay {
    SUNDAY(Calendar.SUNDAY), MONDAY(Calendar.MONDAY),
    TUESDAY(Calendar.TUESDAY),WEDNESDAY(Calendar.WEDNESDAY),
    THURSDAY(Calendar.THURSDAY),FRIDAY(Calendar.FRIDAY),
    SATURDAY(Calendar.SATURDAY);

    int value;
    WeekDay(int calendarWeekday) {
        this.value = calendarWeekday;
    }

    public static WeekDay getWeekdayFromCalendarWeekday(int calendarWeekday) {
        switch (calendarWeekday) {
            case Calendar.SUNDAY:
                return SUNDAY;
            case Calendar.MONDAY:
                return MONDAY;
            case Calendar.TUESDAY:
                return TUESDAY;
            case Calendar.WEDNESDAY:
                return WEDNESDAY;
            case Calendar.THURSDAY:
                return THURSDAY;
            case Calendar.FRIDAY:
                return FRIDAY;
            case Calendar.SATURDAY:
                return SATURDAY;
            default:
                throw new IllegalArgumentException("Day does not exist in Calendar");
        }
    }
}
