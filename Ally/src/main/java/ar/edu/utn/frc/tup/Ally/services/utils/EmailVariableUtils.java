package ar.edu.utn.frc.tup.lc.iv.services.utils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class for operations related to placeholder
 * variables in Email Templates.
 */
public class EmailVariableUtils {

    /**
     * Regex pattern to match placeholders in the format {{placeholder}}.
     */
    private static final Pattern PLACEHOLDER_PATTERN =
            Pattern.compile("\\{\\{(.*?)\\}\\}");

    /**
     * Extracts placeholders from the given email template.
     *
     * @param template the email template string
     * @return a set of keys representing the placeholders in the template.
     */
    public static Set<String> extractPlaceholders(String template) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        return matcher.results()
                .map(match -> match.group(1))
                .collect(Collectors.toSet());
    }
}
