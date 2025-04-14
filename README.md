# Regular Expression 
# Nicologlo Victoria, FAF-233

## Theory

Regular expressions (regex) are powerful patterns used to match and manipulate text strings. This project implements a simplified regex string generator that can:
1. Interpret basic regex patterns
2. Generate random strings that match those patterns
3. Explain the processing steps of pattern interpretation

The generator supports the following regex operators:
- `?` - Matches 0 or 1 of the preceding element
- `*` - Matches 0 or more of the preceding element (up to 5 times in our implementation)
- `+` - Matches 1 or more of the preceding element (1-5 times in our implementation)
- `^n` - Matches exactly n occurrences of the preceding element
- `(a|b)` - Alternation (matches either 'a' or 'b')
- `(a|b|c)` - Multiple alternations (matches 'a', 'b', or 'c')

## Implementation

The implementation consists of two main classes:

### RegexStringGenerator Class

Core functionality includes:

1. **String Generation** (`generateString` method):
   - Processes the regex pattern character by character
   - Handles each operator with appropriate random generation
   - Supports nested operators (like alternation groups with repetition)

2. **Multiple String Generation** (`generateMultipleStrings` method):
   - Generates multiple random strings from the same pattern
   - Returns results as a list

3. **Explanation** (`explainProcessing` method):
   - Provides step-by-step explanation of how the regex is processed
   - Shows how each operator affects the generation

4. **Helper Methods**:
   - `isCharOrDigit`: Checks if a character is alphanumeric
   - `handleRepetition`: Manages repetition operators after alternation groups
   - `getRepetitionCount`: Determines random counts for repetition operators

### Demo Class

Demonstrates the functionality with three example patterns:
1. `(a|b)(c|d)E+G?`
2. `P(Q|R|S)T(U|V|W|X)*Z+`
3. `1(0|1)*2(3|4)^5 36`

For each pattern, it:
1. Explains the processing steps
2. Generates sample matching strings

## Conclusion

This implementation provides a practical tool for:
- Understanding how basic regular expressions work
- Generating test data that matches specific patterns
- Visualizing regex processing through step-by-step explanations

Key strengths:
- Simple and focused implementation
- Clear explanation of processing steps
- Random generation that demonstrates pattern variability

Potential improvements:
- Support for more complex regex features (character classes, ranges, etc.)
- Better error handling for invalid patterns
- More configurable generation parameters

The project successfully demonstrates core regex concepts through practical generation and explanation capabilities.
