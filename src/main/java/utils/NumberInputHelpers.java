package utils;

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

    public static String StringFromInteger(Integer value){
        if(value==null) return "-";
        return Integer.toString(value);
    }

    public static String StringFromDouble(Double value){
        if(value==null) return "-";
        return Double.toString(value);
    }


}
