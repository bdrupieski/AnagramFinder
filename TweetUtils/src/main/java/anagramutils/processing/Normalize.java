package anagramutils.processing;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Normalize {

    private Normalize() {
    }

    private static Pattern alphanumericRegex = Pattern.compile("[^a-z0-9]");

    public static String normalize(String in) {
        String cleaned = Normalizer.normalize(in.trim().toLowerCase(), Normalizer.Form.NFD);
        String transformed = cleaned.replaceAll("ß", "ss").replaceAll("ø", "o");
        return alphanumericRegex.matcher(transformed).replaceAll("");
    }
}
