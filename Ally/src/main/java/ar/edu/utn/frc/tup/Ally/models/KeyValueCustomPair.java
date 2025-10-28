package ar.edu.utn.frc.tup.lc.iv.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a key-value pair for variables in an email template.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueCustomPair {
    /**
     * The key, which typically represents the placeholder in the email template.
     */
    private String key;

    /**
     * The value to replace the placeholder with in the email template.
     */
    private String value;
}

