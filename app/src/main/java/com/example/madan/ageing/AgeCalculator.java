package com.example.madan.ageing;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

public class AgeCalculator {
    private int birthYear;
    private int brithMonth;
    private int birthDay;

    public String calculateAge(String dateOfBirth){
        String[] dates = dateOfBirth.split("-");
        this.birthYear = Integer.parseInt(dates[0]);
        this.brithMonth = Integer.parseInt(dates[1]);
        this.birthDay = Integer.parseInt(dates[2]);

        LocalDate birthday = new LocalDate(this.birthYear, this.brithMonth, this.birthDay);
        LocalDate now = new LocalDate();
        Period period = new Period(birthday, now, PeriodType.yearMonthDay());
        String age = period.getYears() + " years "+ period.getMonths() + " months "+ period.getDays() + " days ";
        return age;
    }
}
