package model;

import model.exceptions.InvalidDateException;
import model.exceptions.ValidationException;

import java.time.YearMonth;
import java.time.Month;
/**
 * Class model.Date represents a date of the calendar including its day month and year
 */
public class Date {
    private int day;
    private int month;
    private int year;

    /**
     * model.Date constructor checks if the day the month and the year given is valid
     * @param day
     * @param month
     * @param year
     * @throws ValidationException if the day month or year are not valid
     */
    Date(int day,int month,int year) throws ValidationException {
        if(year < 2000 || year>3000){
            throw new InvalidDateException("Not valid year given!");
        }
        if(month > 12 || month < 1 ){
            throw new InvalidDateException("Not valid month given!");
        }

        YearMonth ym = YearMonth.of(year, month);
        int days = ym.lengthOfMonth();

        if( day < 0) throw new InvalidDateException("Not valid day given!");

        if( day > days){
            throw new InvalidDateException("Not valid day given for month "+Month.of(ym.getMonthValue()));
        }
    }

    /**
     * @return the day of the date
     */
    public int getDay(){
        return day;
    }
    /**
     * @return the month of the date
     */
    public int getMonth(){
        return month;
    }
    /**
     * @return the year of the date
     */
    public int getYear(){
        return year;
    }


}
