package se.csn.applift.services;

import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;

/**
 * Service for string manipulation operations.
 * This service provides functionality to analyze text content.
 * 
 * It purposes to demonstrate the use of OpenTelemetry annotations for tracing ansd span attributes.
 */
@ApplicationScoped
public class StringService {

    private static final Logger logger = Logger.getLogger(StringService.class.getName());
    private static final String VOWELS = "aeiou";

    /**
     * Counts vowels and consonants in the provided input string.
     * Only alphabetic characters are considered.
     * 
     * @param input the input string to analyze (should be lowercase)
     * @return an array where index 0 contains vowel count and index 1 contains consonant count
     */
    @WithSpan("Count Vowels and Consonants - Service")
    public int[] countVowelsAndConsonants(@SpanAttribute("input.text") String input) {
        logger.info("Analyzing string for vowels and consonants");
        
        if (input == null) {
            logger.warning("Null input provided, returning zero counts");
            return new int[]{0, 0};
        }
        
        int vowels = 0;
        int consonants = 0;

        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                if (VOWELS.indexOf(c) >= 0) {
                    vowels++;
                } else {
                    consonants++;
                }
            }
        }

        logger.info(String.format("Analysis complete: %d vowels, %d consonants", vowels, consonants));
        return new int[]{vowels, consonants};
    }
}
