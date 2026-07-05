package utils;

import java.text.Normalizer;
import java.util.Locale;

public class TextNormalizer {
    public static String normalizeGreekSearchText(String input) {
        if (input == null||input.isBlank()) {
            return "";
        }

        String text = input.trim().toLowerCase(Locale.of("el", "GR"));

        // Break accented characters into base letter + combining mark
        text = Normalizer.normalize(text, Normalizer.Form.NFD);

        // Remove combining diacritical marks
        text = text.replaceAll("\\p{M}+", "");

        // Collapse repeated whitespace
        text = text.replaceAll("\\s+", " ").trim();

        return text;
    }
}
