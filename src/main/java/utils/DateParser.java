package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParser{
    public static LocalDate parseDateFromString(String txt) {
        if (txt == null || txt.isBlank()) {
            return null;
        }

        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("d/M/yyyy");;

        try {
            return LocalDate.parse(txt, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected d/M/yyyy", e);
        }
    }


    public static String getStringDate(LocalDate date){
        if(date==null) return null;
        return date.getDayOfMonth()+"/"+date.getMonthValue()+"/"+date.getYear();
    }
}
