package bql.executor;

import java.util.ArrayList;
import java.util.List;

/**
 * (greedy) Tokenizer for BQL
 */
class BQLTokenizer {

    public enum Type {
        ARRAY,
        COMMA,
        LEFT_BRACKET,
        NUMBER,
        OPERATOR,
        RIGHT_BRACKET,
        STRING,
        VARIABLE,
        WHITESPACE
    }

    public static class Token {
        private String[] text;
        private Type type;

        Token(String[] text) {
            this.text = text;
            this.type = Type.ARRAY;
        }

        Token(String text, Type type) {
            this.text = new String[]{text};
            this.type = type;
        }

        String getText() {
            return text[0];
        }

        String[] getTexts() {
            return text;
        }

        Type getType() {
            return type;
        }

        public String toString() {
            return text[0] + " [" + type.toString() + "]";
        }
    }

    static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int p = 0;
        while (p < input.length()) {
            Token en = next(input, p);
            int n = p + en.getText().length();
            if (en.getType() != Type.WHITESPACE)
                tokens.add(en);
            p = n;
        }
        return tokens;
    }

    private static Token next(String input, int offset) {
        int[] nextPos = {nextKeyword(input, offset),
                nextNumber(input, offset),
                nextWhitespace(input, offset),
                nextComma(input, offset),
                nextLeftBracket(input, offset),
                nextRightBracket(input, offset),
                nextString(input, offset)};
        Type[] types = {Type.OPERATOR, Type.NUMBER, Type.WHITESPACE, Type.COMMA, Type.LEFT_BRACKET, Type.RIGHT_BRACKET, Type.STRING};

        int max = nextPos[0];
        Type type = types[0];
        for (int i = 0; i < nextPos.length; i++) {
            if (nextPos[i] > max) {
                max = nextPos[i];
                type = types[i];
            }
        }
        if (max == offset) {
            max = nextVariable(input, offset);
            type = Type.VARIABLE;
        }
        return new Token(input.substring(offset, max), type);
    }

    private static int nextVariable(String input, int offset) {
        int p = offset;
        while (p < input.length()) {
            char c = input.charAt(p);
            if (c == ',' || c == '[' || c == '{' || c == '(' || c == ']' || c == '}' || c == ')' || c == ' ')
                break;
            p++;
        }
        return p;
    }

    private static int nextString(String input, int offset) {
        if (input.charAt(offset) != '\'')
            return offset;
        int p = offset + 1;
        while (p < input.length() && input.charAt(p) != '\'')
            p++;
        return input.charAt(p) == '\'' ? (p + 1) : offset;
    }

    private static int nextComma(String input, int offset) {
        char c = input.charAt(offset);
        if (c == ',')
            return offset + 1;
        return offset;
    }

    private static int nextLeftBracket(String input, int offset) {
        char c = input.charAt(offset);
        if (c == '(' || c == '[' || c == '{')
            return offset + 1;
        return offset;
    }

    private static int nextRightBracket(String input, int offset) {
        char c = input.charAt(offset);
        if (c == ')' || c == ']' || c == '}')
            return offset + 1;
        return offset;
    }

    private static int nextKeyword(String input, int offset) {
        input = input.toUpperCase();
        String[] operators = {"!=", "*", "<", "<=", "==", ">", ">=", "AND", "ENDS_WITH", "OR", "SELECT", "SORT", "STARTS_WITH", "WHERE"};
        int maxPos = offset;
        for (String operator : operators) {
            int endPos = input.startsWith(operator, offset) ? (offset + operator.length()) : offset;
            maxPos = Math.max(maxPos, endPos);
        }
        return maxPos;
    }

    private static int nextNumber(String input, int offset) {
        int p = offset;
        while (p < input.length() && (Character.isDigit(input.charAt(p)) || input.charAt(p) == '.'))
            p++;
        String subs = input.substring(offset, p);
        try {
            Double.parseDouble(subs);
            return p;
        } catch (NumberFormatException ex) {
            return offset;
        }
    }

    private static int nextWhitespace(String input, int offset) {
        int p = offset;
        while (p < input.length() && (Character.isWhitespace(input.charAt(p)) || input.charAt(p) == '\n'))
            p++;
        return p;
    }
}
