package org.leapyear;

import java.util.Calendar;

public class LeapYearCalculator {

    public static boolean isLeapYear(int year) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if (year < 0 || year > currentYear) {
            return false;
        }

        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            return true;
        }

        return false;
    }
}
