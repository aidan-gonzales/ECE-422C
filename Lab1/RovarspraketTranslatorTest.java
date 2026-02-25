import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for RovarspraketTranslator.
 * 
 * Run these tests frequently as you implement each method.
 * All tests should pass before submitting your solution.
 */
class RovarspraketTranslatorTest {

    // ==================== isVowel Tests ====================

    @Test
    @DisplayName("isVowel: lowercase vowels return true")
    void testIsVowel_lowercaseVowels() {
        assertTrue(RovarspraketTranslator.isVowel('a'));
        assertTrue(RovarspraketTranslator.isVowel('e'));
        assertTrue(RovarspraketTranslator.isVowel('i'));
        assertTrue(RovarspraketTranslator.isVowel('o'));
        assertTrue(RovarspraketTranslator.isVowel('u'));
    }

    @Test
    @DisplayName("isVowel: uppercase vowels return true")
    void testIsVowel_uppercaseVowels() {
        assertTrue(RovarspraketTranslator.isVowel('A'));
        assertTrue(RovarspraketTranslator.isVowel('E'));
        assertTrue(RovarspraketTranslator.isVowel('I'));
        assertTrue(RovarspraketTranslator.isVowel('O'));
        assertTrue(RovarspraketTranslator.isVowel('U'));
    }

    @Test
    @DisplayName("isVowel: consonants return false")
    void testIsVowel_consonants() {
        assertFalse(RovarspraketTranslator.isVowel('b'));
        assertFalse(RovarspraketTranslator.isVowel('c'));
        assertFalse(RovarspraketTranslator.isVowel('z'));
        assertFalse(RovarspraketTranslator.isVowel('B'));
        assertFalse(RovarspraketTranslator.isVowel('Z'));
    }

    @Test
    @DisplayName("isVowel: non-letters return false")
    void testIsVowel_nonLetters() {
        assertFalse(RovarspraketTranslator.isVowel('1'));
        assertFalse(RovarspraketTranslator.isVowel('!'));
        assertFalse(RovarspraketTranslator.isVowel(' '));
        assertFalse(RovarspraketTranslator.isVowel('\n'));
    }

    // ==================== isConsonant Tests ====================

    @Test
    @DisplayName("isConsonant: lowercase consonants return true")
    void testIsConsonant_lowercaseConsonants() {
        assertTrue(RovarspraketTranslator.isConsonant('b'));
        assertTrue(RovarspraketTranslator.isConsonant('c'));
        assertTrue(RovarspraketTranslator.isConsonant('d'));
        assertTrue(RovarspraketTranslator.isConsonant('z'));
    }

    @Test
    @DisplayName("isConsonant: uppercase consonants return true")
    void testIsConsonant_uppercaseConsonants() {
        assertTrue(RovarspraketTranslator.isConsonant('B'));
        assertTrue(RovarspraketTranslator.isConsonant('C'));
        assertTrue(RovarspraketTranslator.isConsonant('D'));
        assertTrue(RovarspraketTranslator.isConsonant('Z'));
    }

    @Test
    @DisplayName("isConsonant: vowels return false")
    void testIsConsonant_vowels() {
        assertFalse(RovarspraketTranslator.isConsonant('a'));
        assertFalse(RovarspraketTranslator.isConsonant('e'));
        assertFalse(RovarspraketTranslator.isConsonant('i'));
        assertFalse(RovarspraketTranslator.isConsonant('A'));
        assertFalse(RovarspraketTranslator.isConsonant('E'));
    }

    @Test
    @DisplayName("isConsonant: non-letters return false")
    void testIsConsonant_nonLetters() {
        assertFalse(RovarspraketTranslator.isConsonant('1'));
        assertFalse(RovarspraketTranslator.isConsonant('!'));
        assertFalse(RovarspraketTranslator.isConsonant(' '));
        assertFalse(RovarspraketTranslator.isConsonant('-'));
    }

    // ==================== encodeChar Tests ====================

    @Test
    @DisplayName("encodeChar: lowercase consonants encode correctly")
    void testEncodeChar_lowercaseConsonants() {
        assertEquals("bob", RovarspraketTranslator.encodeChar('b'));
        assertEquals("hoh", RovarspraketTranslator.encodeChar('h'));
        assertEquals("lol", RovarspraketTranslator.encodeChar('l'));
        assertEquals("zoz", RovarspraketTranslator.encodeChar('z'));
    }

    @Test
    @DisplayName("encodeChar: uppercase consonants encode with preserved case")
    void testEncodeChar_uppercaseConsonants() {
        assertEquals("BoB", RovarspraketTranslator.encodeChar('B'));
        assertEquals("HoH", RovarspraketTranslator.encodeChar('H'));
        assertEquals("LoL", RovarspraketTranslator.encodeChar('L'));
        assertEquals("ZoZ", RovarspraketTranslator.encodeChar('Z'));
    }

    @Test
    @DisplayName("encodeChar: vowels remain unchanged")
    void testEncodeChar_vowels() {
        assertEquals("a", RovarspraketTranslator.encodeChar('a'));
        assertEquals("e", RovarspraketTranslator.encodeChar('e'));
        assertEquals("A", RovarspraketTranslator.encodeChar('A'));
        assertEquals("O", RovarspraketTranslator.encodeChar('O'));
    }

    @Test
    @DisplayName("encodeChar: non-letters remain unchanged")
    void testEncodeChar_nonLetters() {
        assertEquals("1", RovarspraketTranslator.encodeChar('1'));
        assertEquals("!", RovarspraketTranslator.encodeChar('!'));
        assertEquals(" ", RovarspraketTranslator.encodeChar(' '));
        assertEquals(",", RovarspraketTranslator.encodeChar(','));
    }

    // ==================== encode Tests ====================

    @Test
    @DisplayName("encode: simple lowercase word")
    void testEncode_simpleLowercase() {
        assertEquals("hohelollolo", RovarspraketTranslator.encode("hello"));
    }

    @Test
    @DisplayName("encode: mixed case word")
    void testEncode_mixedCase() {
        assertEquals("HoHelollolo", RovarspraketTranslator.encode("Hello"));
        assertEquals("JoJavova", RovarspraketTranslator.encode("Java"));
    }

    @Test
    @DisplayName("encode: sentence with punctuation")
    void testEncode_withPunctuation() {
        assertEquals("HoHelollolo, WoWororloldod!", 
                     RovarspraketTranslator.encode("Hello, World!"));
    }

    @Test
    @DisplayName("encode: empty string returns empty")
    void testEncode_emptyString() {
        assertEquals("", RovarspraketTranslator.encode(""));
    }

    @Test
    @DisplayName("encode: null returns null")
    void testEncode_null() {
        assertNull(RovarspraketTranslator.encode(null));
    }

    @Test
    @DisplayName("encode: all vowels remain unchanged")
    void testEncode_allVowels() {
        assertEquals("aeiou", RovarspraketTranslator.encode("aeiou"));
        assertEquals("AEIOU", RovarspraketTranslator.encode("AEIOU"));
    }

    @Test
    @DisplayName("encode: all consonants")
    void testEncode_allConsonants() {
        assertEquals("bobcocdod", RovarspraketTranslator.encode("bcd"));
    }

    @Test
    @DisplayName("encode: numbers remain unchanged")
    void testEncode_numbers() {
        assertEquals("123", RovarspraketTranslator.encode("123"));
        assertEquals("ToTesostot123", RovarspraketTranslator.encode("Test123"));
    }

    @ParameterizedTest
    @CsvSource({
        "a, a",
        "b, bob",
        "hi, hohi",
        "abc, abobcoc"
    })
    @DisplayName("encode: parameterized basic cases")
    void testEncode_parameterized(String input, String expected) {
        assertEquals(expected, RovarspraketTranslator.encode(input));
    }

    // ==================== decode Tests ====================

    @Test
    @DisplayName("decode: simple encoded word")
    void testDecode_simple() {
        assertEquals("hello", RovarspraketTranslator.decode("hohelollolo"));
    }

    @Test
    @DisplayName("decode: mixed case encoded word")
    void testDecode_mixedCase() {
        assertEquals("Hello", RovarspraketTranslator.decode("HoHelollolo"));
        assertEquals("Java", RovarspraketTranslator.decode("JoJavova"));
    }

    @Test
    @DisplayName("decode: sentence with punctuation")
    void testDecode_withPunctuation() {
        assertEquals("Hello, World!", 
                     RovarspraketTranslator.decode("HoHelollolo, WoWororloldod!"));
    }

    @Test
    @DisplayName("decode: empty string returns empty")
    void testDecode_emptyString() {
        assertEquals("", RovarspraketTranslator.decode(""));
    }

    @Test
    @DisplayName("decode: null returns null")
    void testDecode_null() {
        assertNull(RovarspraketTranslator.decode(null));
    }

    @Test
    @DisplayName("decode: all vowels remain unchanged")
    void testDecode_allVowels() {
        assertEquals("aeiou", RovarspraketTranslator.decode("aeiou"));
    }

    @Test
    @DisplayName("decode: numbers remain unchanged")
    void testDecode_numbers() {
        assertEquals("123", RovarspraketTranslator.decode("123"));
    }

    // ==================== Round-trip Tests ====================

    @ParameterizedTest
    @ValueSource(strings = {
        "hello",
        "Hello",
        "Java",
        "Hello, World!",
        "The quick brown fox jumps over the lazy dog.",
        "ECE 422C",
        "aeiouAEIOU",
        "bcdfghjklmnpqrstvwxyz",
        "MixedCASE123!@#"
    })
    @DisplayName("round-trip: encode then decode returns original")
    void testRoundTrip(String original) {
        String encoded = RovarspraketTranslator.encode(original);
        String decoded = RovarspraketTranslator.decode(encoded);
        assertEquals(original, decoded, 
            String.format("Round-trip failed: '%s' -> '%s' -> '%s'", 
                          original, encoded, decoded));
    }

    @Test
    @DisplayName("round-trip: empty string")
    void testRoundTrip_empty() {
        String original = "";
        assertEquals(original, RovarspraketTranslator.decode(RovarspraketTranslator.encode(original)));
    }

    // ==================== Edge Case Tests ====================

    @Test
    @DisplayName("edge case: single vowel")
    void testEdgeCase_singleVowel() {
        assertEquals("a", RovarspraketTranslator.encode("a"));
        assertEquals("a", RovarspraketTranslator.decode("a"));
    }

    @Test
    @DisplayName("edge case: single consonant")
    void testEdgeCase_singleConsonant() {
        assertEquals("bob", RovarspraketTranslator.encode("b"));
        assertEquals("b", RovarspraketTranslator.decode("bob"));
    }

    @Test
    @DisplayName("edge case: only whitespace")
    void testEdgeCase_onlyWhitespace() {
        assertEquals("   ", RovarspraketTranslator.encode("   "));
        assertEquals("   ", RovarspraketTranslator.decode("   "));
    }

    @Test
    @DisplayName("edge case: consecutive consonants")
    void testEdgeCase_consecutiveConsonants() {
        assertEquals("sostotrorinongog", RovarspraketTranslator.encode("string"));
        assertEquals("string", RovarspraketTranslator.decode("sostotrorinongog"));
    }

    @Test
    @DisplayName("edge case: all same consonant")
    void testEdgeCase_sameLetter() {
        assertEquals("bobbobbob", RovarspraketTranslator.encode("bbb"));
        assertEquals("bbb", RovarspraketTranslator.decode("bobbobbob"));
    }
}
