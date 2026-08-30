package utils;

import model.exceptions.ValidationException;

public class NumberInputHelpers {
    public static Integer parseInteger(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        try {
           return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static Double parseDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        try {
            return Double.parseDouble(text.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses an optional integer field and returns a field-specific error
     */
    public static Integer parseInteger(String text, String fieldName) {
        if (text == null || text.isBlank() || text.equals("-")) {
            return null;
        }

        String value = text.trim();
        if (!value.matches("\\d+")) {
            throw new ValidationException("Το πεδίο «" + fieldName + "» πρέπει να περιέχει μόνο αριθμούς!");
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ValidationException("Η τιμή στο πεδίο «" + fieldName + "» είναι πολύ μεγάλη!", e);
        }
    }

    /**
     * Parses an optional decimal field accepting a comma or a dot
     */
    public static Double parseDouble(String text, String fieldName) {
        if (text == null || text.isBlank() || text.equals("-")) {
            return null;
        }

        String value = text.trim();
        if (!value.matches("\\d+(?:[.,]\\d+)?")) {
            throw new ValidationException(
                    "Το πεδίο «" + fieldName + "» πρέπει να περιέχει έγκυρο αριθμό!"
            );
        }

        try {
            return Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValidationException("Μη έγκυρη τιμή στο πεδίο «" + fieldName + "»!", e);
        }
    }

    public static String StringFromInteger(Integer value){
        if(value==null) return "-";
        return Integer.toString(value);
    }

    public static String StringFromDouble(Double value){
        if(value==null) return "-";
        return Double.toString(value);
    }


}
