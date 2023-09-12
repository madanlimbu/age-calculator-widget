package com.madan.madan.ageing;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

/**
 * Class to Calculate Age Using Joda Library.
 *
 */

public class AgeCalculator {
    private String dateOfBirthStringSplitter = "-";
    private int birthYear;
    private int brithMonth;
    private int birthDay;

    /**
     * Calculate age using dateOfBirth, and current day using joda libary.
     *
     *
     * @param dateOfBirth
     * @return
     */
    public String calculateAge(String dateOfBirth){
        String age = "No Correct Date Of Birth Set Yet (YYYY-MM-DD)";
        try {
            setIntDateOfBirthUsingString(dateOfBirth);
            LocalDate birthday = new LocalDate(this.birthYear, this.brithMonth, this.birthDay);
            LocalDate now = new LocalDate();
            Period period = new Period(birthday, now, PeriodType.yearMonthDay());
            age = period.getYears() + " years "+ period.getMonths() + " months "+ period.getDays() + " days ";
        } catch (Exception e){
            return age;
        }
        return age;
    }

    /**
     * Date of Birth from string to int variables.
     *
     * @param dateOfBirth
     */
    public void setIntDateOfBirthUsingString(String dateOfBirth){
        String[] dates = dateOfBirth.split(this.dateOfBirthStringSplitter);
        this.birthYear = Integer.parseInt(dates[0]);
        this.brithMonth = Integer.parseInt(dates[1]);
        this.birthDay = Integer.parseInt(dates[2]);
    }
}
