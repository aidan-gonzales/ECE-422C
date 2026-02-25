# ECE 422C Lab: Rövarspråket Translator - Due Janary 26th @ 11:59pm

## Overview

In this lab, you will implement a translator for **Rövarspråket** (Swedish for "Robber's Language"), a playful Swedish language game. The rules are simple: every consonant is doubled with an 'o' inserted between. Vowels remain unchanged.

For example:
- `"hello"` → `"hohelollolo"`
- `"Java"` → `"Jojavoava"`
- `"rövarspråket"` → `"rorövovarorsospoproråkoketot"`

This lab reinforces fundamental string manipulation techniques including character-by-character processing, StringBuilder usage, case handling, and edge case management.

---

## Learning Objectives

By completing this lab, you will:

1. **Practice character classification** — Distinguish vowels from consonants programmatically
2. **Apply StringBuilder for efficient string building** — Understand why concatenation in loops is inefficient
3. **Handle case sensitivity** — Preserve original letter casing in transformations
4. **Process edge cases** — Handle punctuation, numbers, whitespace, and Unicode characters
5. **Write clean, well-documented code** — Follow Java conventions and write meaningful Javadoc
6. **Implement bidirectional translation** — Build both encoder and decoder methods

---

## Background

### The Rules of Rövarspråket

1. **Consonants** are transformed by doubling them with 'o' between:
   - `b` → `bob`, `c` → `coc`, `d` → `dod`, etc.
   - `B` → `BoB`, `C` → `CoC`, `D` → `DoD`, etc. (case preserved)

2. **Vowels** (a, e, i, o, u) remain unchanged:
   - `a` → `a`, `E` → `E`

3. **Non-alphabetic characters** pass through unchanged:
   - `!` → `!`, `5` → `5`, ` ` → ` `

4. **Case is preserved**:
   - Uppercase consonants produce uppercase transformations
   - `H` → `HoH` (not `HOH` or `hoh`)

### Why StringBuilder?

When building strings character by character, using `+=` creates a new String object each iteration:

```java
// BAD - O(n²) time complexity
String result = "";
for (char c : input.toCharArray()) {
    result += transform(c);  // Creates new String each time!
}

// GOOD - O(n) time complexity  
StringBuilder sb = new StringBuilder();
for (char c : input.toCharArray()) {
    sb.append(transform(c));  // Modifies buffer in place
}
return sb.toString();
```

---

## Specification

You must implement the following methods in `RovarspraketTranslator.java`:

### Required Methods

#### `public static boolean isVowel(char c)`
Returns `true` if the character is a vowel (a, e, i, o, u), case-insensitive. Returns `false` otherwise.

#### `public static boolean isConsonant(char c)`
Returns `true` if the character is a consonant (any letter that is not a vowel). Returns `false` for non-letter characters.

#### `public static String encodeChar(char c)`
Encodes a single character according to Rövarspråket rules:
- Consonants: return the consonant + 'o' + consonant (preserving case)
- Vowels and non-letters: return the character unchanged as a String

#### `public static String encode(String input)`
Encodes an entire string to Rövarspråket. Process each character and concatenate results.

#### `public static String decode(String input)`
Decodes a Rövarspråket string back to plain text. This is trickier than encoding because you must:
1. Recognize consonant patterns (e.g., `bob` → `b`)
2. Validate that patterns are well-formed (middle char is 'o', first and last match)
3. Handle invalid input gracefully

### Method Signatures

```java
public class RovarspraketTranslator {
    public static boolean isVowel(char c)
    public static boolean isConsonant(char c)
    public static String encodeChar(char c)
    public static String encode(String input)
    public static String decode(String input)
}
```

---

## Examples

### Encoding Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"hi"` | `"hohi"` | 'h' → "hoh", 'i' → "i" |
| `"Hello"` | `"HoHelollolo"` | 'H' → "HoH", 'e' → "e", 'l' → "lol", 'l' → "lol", 'o' → "o" |
| `"Java"` | `"JoJavova"` | 'J' → "JoJ", 'a' → "a", 'v' → "vov", 'a' → "a" |
| `"ABC"` | `"ABoBCoC"` | 'A' → "A", 'B' → "BoB", 'C' → "CoC" |
| `"Hello, World!"` | `"HoHelollolo, WoWororloldod!"` | Punctuation preserved |
| `""` | `""` | Empty string returns empty |
| `"123"` | `"123"` | Numbers unchanged |
| `"aeiou"` | `"aeiou"` | All vowels, no change |

### Decoding Examples

| Input | Output | Explanation |
|-------|--------|-------------|
| `"hohi"` | `"hi"` | "hoh" → 'h', "i" → 'i' |
| `"HoHelollolo"` | `"Hello"` | Reverse of encoding |
| `"JoJavova"` | `"Java"` | Reverse of encoding |
| `""` | `""` | Empty returns empty |
| `"aeiou"` | `"aeiou"` | All vowels, no patterns |

---

## Implementation Order

We recommend implementing the methods in this order:

1. **`isVowel`** — Simplest, foundation for others
2. **`isConsonant`** — Uses `isVowel` as helper
3. **`encodeChar`** — Single character transformation
4. **`encode`** — Applies `encodeChar` to whole string
5. **`decode`** — Most complex, implement last

---

## Testing Your Code

### Running the Provided Tests

Compile and run the test file:

```bash
javac -cp .:junit-platform-console-standalone.jar *.java
java -jar junit-platform-console-standalone.jar --class-path . --scan-classpath
```

Or if your IDE supports JUnit 5, simply run `RovarspraketTranslatorTest.java`.

### Writing Your Own Tests

We strongly encourage you to write additional test cases! Consider:

- Very long strings
- Strings with only vowels
- Strings with only consonants
- Strings with mixed Unicode characters (é, ñ, etc.)
- Malformed Rövarspråket strings for decode (e.g., `"bob"` is valid, but `"bab"` is not)

---

## Submission Checklist

Before submitting, verify:

- [ ] All five required methods are implemented
- [ ] `isVowel` handles both uppercase and lowercase
- [ ] `isConsonant` returns false for non-letters
- [ ] `encodeChar` preserves case correctly
- [ ] `encode` uses StringBuilder (not string concatenation)
- [ ] `decode` correctly reverses encoding
- [ ] `decode` handles invalid patterns gracefully
- [ ] Null input is handled appropriately
- [ ] Empty string input returns empty string
- [ ] All provided test cases pass
- [ ] Code follows Java naming conventions
- [ ] Methods have appropriate Javadoc comments

---
## Submitting to Gradescope 

- [ ] You shold only submit the file `RovarspraketTranslator.java` to Gradescope
- [ ] Do not submit any other files. 
- [ ] Do not zip your submission. 
- [ ] Do not include test files in your submission.
- [ ] Make sure your EID and Name appear at the top of the file in the appropriate marked place 

---

## Files Provided

```
rovarspraket_lab/
├── README.md                         # This file
├── starter_files/
│   ├── RovarspraketTranslator.java   # Skeleton to complete
│   └── RovarspraketTranslatorTest.java  # JUnit 5 tests
```

---

## Getting Help

1. Re-read the specification carefully
2. Trace through examples by hand
3. Add print statements to debug
4. Test individual methods before combining them
5. Ask on Ed Discussion or attend office hours

Good luck, and have fun speaking like a Swedish robber! 🇸🇪
