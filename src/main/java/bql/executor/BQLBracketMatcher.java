package bql.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This class handles bracket related logic for BQL
 */
class BQLBracketMatcher {

    /**
     * Test whether two tokens are matching brackets
     *
     * @param left  left bracket token
     * @param right right bracket token
     * @return true iff the left bracket token matches the right bracket token, false otherwise
     */
    private static boolean isMatchingBracket(BQLTokenizer.Token left, BQLTokenizer.Token right) {
        String[] lOps = {"[", "(", "{"};
        String[] rOps = {"]", ")", "}"};
        for (int i = 0; i < lOps.length; i++) {
            if (left.getText().equals(lOps[i]))
                return right.getText().equals(rOps[i]);
        }
        return false;
    }

    /**
     * Builds Map&lt;Integer, Integer&gt; such that any bracket position can be queried, and the matching bracket position returned
     *
     * @param tokens input tokens
     * @return Map of matching bracket positions
     */
    static Map<Integer, Integer> matchingBrackets(List<BQLTokenizer.Token> tokens) {
        Map<Integer, Integer> retval = new HashMap<>();
        Stack<Object> tmp = new Stack<>();
        for (int i = 0; i < tokens.size(); i++) {
            BQLTokenizer.Token t = tokens.get(i);
            if (t.getType() == BQLTokenizer.Type.LEFT_BRACKET)
                tmp.push(new Object[]{t, i});
            else if (t.getType() == BQLTokenizer.Type.RIGHT_BRACKET) {
                if (tmp.isEmpty())
                    throw new IllegalArgumentException("Mismatched parenthesis");

                Object[] objs = (Object[]) tmp.pop();
                BQLTokenizer.Token l = (BQLTokenizer.Token) objs[0];
                Integer leftPos = (Integer) objs[1];
                if (!isMatchingBracket(l, t))
                    throw new IllegalArgumentException("Mismatched parenthesis");
                retval.put(leftPos, i);
                retval.put(i, leftPos);
            }
        }
        return retval;
    }
}
