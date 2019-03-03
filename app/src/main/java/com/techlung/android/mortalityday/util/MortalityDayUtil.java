package com.techlung.android.mortalityday.util;

import android.content.Context;

import com.techlung.android.mortalityday.R;
import com.techlung.android.mortalityday.enums.Frequency;
import com.techlung.android.mortalityday.settings.Preferences;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class MortalityDayUtil {

    private MortalityDayUtil() {

    }

    public static boolean isMortalityDay() {
        Calendar day = new GregorianCalendar();
        day.setTime(new Date());
        return isMortalityDay(day);
    }

    private static boolean isMortalityDay(Calendar day) {
        int weekDay = day.get(Calendar.DAY_OF_WEEK);

        if (Preferences.getFrequency() == Frequency.ONCE_A_WEEK) {
            return weekDay == Preferences.getDay1();
        } else if (Preferences.getFrequency() == Frequency.TWICE_A_WEEK) {
            return weekDay == Preferences.getDay1() || weekDay == Preferences.getDay2();
        } else {
            return true;
        }

    }

    /**
     * Get next mortality day. skip current day if today is a mortality day.
     */
    public static Calendar getNextMortalityDay() {
        Calendar day = new GregorianCalendar();
        day.setTime(new Date());

        day.add(Calendar.MINUTE, 1440); // add one Day

        if (Preferences.getFrequency() != Frequency.EVERY_DAY) {
            while (!isMortalityDay(day)) {
                day.add(Calendar.MINUTE, 1440); // add one Day
            }
        }

        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        return day;
    }

    public static MortalityDayQuote getQuote(Context context) {
        String[] messages = context.getResources().getStringArray(R.array.notification_messages);
        String[] authors = context.getResources().getStringArray(R.array.notification_authors);

        int index = (int) (Math.random() * (messages.length));
        MortalityDayQuote quote = new MortalityDayQuote();

        quote.message = messages[index];
        quote.author = authors[index];

        return quote;
    }

    public static class MortalityDayQuote {
        public String message;
        public String author;
    }

}
