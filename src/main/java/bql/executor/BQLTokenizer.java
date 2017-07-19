package bql.executor;

import java.util.ArrayList;
import java.util.List;

/**
 * (greedy) Tokenizer for BQL
 */
public class BQLTokenizer {

    public enum Type
    {
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

    public static class Token
    {
        private String[] text;
        private Type type;
        public Token(String[] text)
        {
            this.text = text;
            this.type = Type.ARRAY;
        }
        public Token(String text, Type type)
        {
            this.text = new String[]{text};
            this.type = type;
        }
        public String getText()
        {
            return text[0];
        }
        public String[] getTexts()
        {
            return text;
        }
        public Type getType()
        {
            return type;
        }
        public String toString(){return text[0] + " [" + type.toString() + "]";}
    }

    public static List<Token> tokenize(String input)
    {
        List<Token> tokens = new ArrayList<>();
        int p = 0;
        while(p < input.length())
        {
            Token en = next(input, p);
            int n = p + en.getText().length();
            if(en.getType() != Type.WHITESPACE)
                tokens.add(en);
            p = n;
        }
        return tokens;
    }

    public static Token next(String input, int offset)
    {
        int[] nextPos = {   nextKeyword(input, offset),
                            nextNumber(input, offset),
                            nextWhitespace(input, offset),
                            nextComma(input, offset),
                            nextLeftBracket(input, offset),
                            nextRightBracket(input, offset),
                            nextString(input, offset)};
        Type[] types = {Type.OPERATOR, Type.NUMBER, Type.WHITESPACE, Type.COMMA, Type.LEFT_BRACKET, Type.RIGHT_BRACKET, Type.STRING};

        int max = nextPos[0];
        Type type = types[0];
        for(int i=0;i<nextPos.length;i++) {
            if(nextPos[i] > max)
            {
                max = nextPos[i];
                type = types[i];
            }
        }
        if(max == offset) {
            max = nextVariable(input, offset);
            type = Type.VARIABLE;
        }
        return new Token(input.substring(offset, max), type);
    }

    public static int nextVariable(String input, int offset)
    {
        int p = offset;
        while(p < input.length()) {
            char c = input.charAt(p);
            if(c==',' || c=='[' || c=='{' || c=='(' || c==']' || c=='}' || c==')' || c==' ')
                break;
            p++;
        }
        return p;
    }

    public static int nextString(String input, int offset)
    {
        if(input.charAt(offset) != '\'')
            return offset;
        int p = offset + 1;
        while(p < input.length() && input.charAt(p) != '\'')
            p++;
        return input.charAt(p) == '\'' ? (p+1) : offset;
    }

    public static int nextComma(String input, int offset)
    {
        char c = input.charAt(offset);
        if(c == ',')
            return offset + 1;
        return offset;
    }

    public static int nextLeftBracket(String input, int offset)
    {
        char c = input.charAt(offset);
        if(c == '(' || c == '[' || c=='{')
            return offset + 1;
        return offset;
    }

    public static int nextRightBracket(String input, int offset)
    {
        char c = input.charAt(offset);
        if(c == ')' || c == ']' || c=='}')
            return offset + 1;
        return offset;
    }

    public static int nextKeyword(String input, int offset)
    {
        input = input.toUpperCase();
        String[] operators = {"WHERE","AND","OR","SELECT",">",">=","<","<=","==","!=","SORT","*"};
        int maxPos = offset;
        for(int i=0;i<operators.length;i++)
        {
            int endPos = input.startsWith(operators[i], offset) ? (offset + operators[i].length()) : offset;
            maxPos = java.lang.Math.max(maxPos, endPos);
        }
        return maxPos;
    }

    public static int nextNumber(String input, int offset)
    {
        int p = offset;
        while(p < input.length() && (Character.isDigit(input.charAt(p)) || input.charAt(p) == '.'))
            p++;
        String subs = input.substring(offset, p);
        try{
            Double.parseDouble(subs);
            return p;
        }catch(Exception ex)
        {
            return offset;
        }
    }

    public static int nextWhitespace(String input, int offset)
    {
        int p = offset;
        while(p < input.length() && (Character.isWhitespace(input.charAt(p))  || input.charAt(p) == '\n'))
            p++;
        return p;
    }
}
