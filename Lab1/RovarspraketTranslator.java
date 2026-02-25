/**
 * ECE 422C - Rövarspråket Translator
 * 
 * This class implements a translator for Rövarspråket ("Robber's Language"),
 * a Swedish language game where consonants are doubled with 'o' between them.
 * 
 * Example: "hello" → "hohelollolo"
 * 
 * @author Aidan Gonzales
 * @EID AGG3498
 */
public class RovarspraketTranslator {

    /**
     * Determines if a character is a vowel (a, e, i, o, u).
     * This method should be case-insensitive.
     * 
     * Examples:
     *   isVowel('a') → true
     *   isVowel('A') → true
     *   isVowel('b') → false
     *   isVowel('5') → false
     * 
     * @param c the character to check
     * @return true if c is a vowel, false otherwise
     */
    public static boolean isVowel(char c) {
        // TODO: Implement this method
        // Hint: Consider using Character.toLowerCase() to simplify your logic

        c = Character.toLowerCase(c);
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
            return true;
        }

        return false;
    }

    /**
     * Determines if a character is a consonant.
     * A consonant is any letter that is NOT a vowel.
     * Non-letter characters (digits, punctuation, etc.) are NOT consonants.
     * 
     * Examples:
     *   isConsonant('b') → true
     *   isConsonant('B') → true
     *   isConsonant('a') → false
     *   isConsonant('5') → false
     *   isConsonant('!') → false
     * 
     * @param c the character to check
     * @return true if c is a consonant, false otherwise
     */
    public static boolean isConsonant(char c) {
        // TODO: Implement this method
        // Hint: Use Character.isLetter() and your isVowel() method

        if (Character.isLetter(c) == false) {
            return false;
        }

        if (isVowel(c) == true) {
            return false;
        }
        
        return true;
    }

    /**
     * Encodes a single character according to Rövarspråket rules:
     * - Consonants become: consonant + 'o' + consonant (preserving case)
     * - Vowels and non-letters remain unchanged
     * 
     * Examples:
     *   encodeChar('h') → "hoh"
     *   encodeChar('H') → "HoH"
     *   encodeChar('a') → "a"
     *   encodeChar('!') → "!"
     * 
     * @param c the character to encode
     * @return the encoded string representation
     */
    public static String encodeChar(char c) {
        // TODO: Implement this method
        // Hint: The 'o' is always lowercase, but preserve the case of the consonant

        String ret;

        if (isVowel(c) || !Character.isLetter(c)) {
            ret = "" + c;
        } else {
            ret = c + "o" + c;
        }
        
        return ret;
    }

    /**
     * Encodes an entire string to Rövarspråket.
     * 
     * Examples:
     *   encode("hello") → "hohelollolo"
     *   encode("Java") → "JoJavova"
     *   encode("Hello, World!") → "HoHelollolo, WoWororloldod!"
     *   encode("") → ""
     *   encode("aeiou") → "aeiou"
     * 
     * @param input the string to encode (may be null)
     * @return the encoded string, or null if input is null
     */
    public static String encode(String input) {
        // TODO: Implement this method
        // Hint: Use StringBuilder for efficiency, not string concatenation
        // Hint: Handle null input appropriately

        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder encoded = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            encoded.append(encodeChar(input.charAt(i)));
        }
        
        return encoded.toString();
    }

    /**
     * Decodes a Rövarspråket string back to plain text.
     * 
     * This method reverses the encoding process:
     * - Recognizes consonant patterns (XoX where X is a consonant) and converts to X
     * - Leaves vowels and non-letters unchanged
     * 
     * Examples:
     *   decode("hohelollolo") → "hello"
     *   decode("JoJavova") → "Java"
     *   decode("HoHelollolo, WoWororloldod!") → "Hello, World!"
     *   decode("") → ""
     *   decode("aeiou") → "aeiou"
     * 
     * Implementation notes:
     * - A valid consonant pattern has: consonant + 'o' (or 'O') + same consonant (same case)
     * - If a pattern is invalid (e.g., "bab" or "bob" at end of string where third char differs),
     *   treat the first character as a regular character and continue
     * 
     * @param input the Rövarspråket string to decode (may be null)
     * @return the decoded string, or null if input is null
     */
    public static String decode(String input) {
        // TODO: Implement this method
        // Hint: Use an index-based loop so you can skip ahead when you find a pattern
        // Hint: Check if there are at least 3 characters remaining before checking for a pattern
        // Hint: Verify all three parts of the pattern: first consonant, 'o', matching consonant

        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder decoded = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            if (isConsonant(input.charAt(i))) {
                if (i + 2 < input.length()) {
                    if ((input.charAt(i + 2) == input.charAt(i)) && ((input.charAt(i + 1) == 'O') || (input.charAt(i + 1) == 'o'))) {
                        decoded.append(input.charAt(i));
                        i += 2;
                    } else {
                        decoded.append(input.charAt(i));
                    }
                }
            } else {
                decoded.append(input.charAt(i));
            }
        }
        
        return decoded.toString();
    }

    /**
     * Main method for simple interactive testing.
     * This is provided for your convenience during development.
     */
    public static void main(String[] args) {
        // Test encoding
        System.out.println("=== Encoding Tests ===");
        String[] testInputs = {"hello", "Java", "Hello, World!", "", "aeiou", "123"};
        
        for (String test : testInputs) {
            String encoded = encode(test);
            System.out.printf("encode(\"%s\") = \"%s\"%n", test, encoded);
        }
        
        System.out.println();
        
        // Test decoding
        System.out.println("=== Decoding Tests ===");
        String[] encodedInputs = {"hohelollolo", "Jojavoava", "HoHelollolo, WoWororloldod!", "", "aeiou"};
        
        for (String test : encodedInputs) {
            String decoded = decode(test);
            System.out.printf("decode(\"%s\") = \"%s\"%n", test, decoded);
        }
        
        System.out.println();
        
        // Test round-trip
        System.out.println("=== Round-Trip Tests ===");
        for (String test : testInputs) {
            String encoded = encode(test);
            String decoded = decode(encoded);
            boolean matches = test.equals(decoded);
            System.out.printf("\"%s\" -> \"%s\" -> \"%s\" [%s]%n", 
                test, encoded, decoded, matches ? "PASS" : "FAIL");
        }
    }
}
