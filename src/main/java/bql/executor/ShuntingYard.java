package bql.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * ShuntingYard implementation for BQL
 * A Shunting Yard algorithm will take an expression in infix notation and convert it to postfix.
 * Postfix is a lot easier to process for building abstract syntax trees.
 */
public class ShuntingYard {

    /**
     * @param infix
     * @return
     */
    public static List<BQLTokenizer.Token> postfix(List<BQLTokenizer.Token> infix) {
        Map<Integer, Integer> matchingBrackets = BQLBracketMatcher.matchingBrackets(infix);
        Stack<BQLTokenizer.Token> stk = new Stack<>();
        List<BQLTokenizer.Token> output = new ArrayList<>();

        for (int i = 0; i < infix.size(); i++) {
            BQLTokenizer.Token t = infix.get(i);

            if (t.getType() == BQLTokenizer.Type.WHITESPACE)
                continue;

            // if the token is a number or literal, or variable, push it to the output queue
            if (t.getType() == BQLTokenizer.Type.NUMBER || t.getType() == BQLTokenizer.Type.STRING || t.getType() == BQLTokenizer.Type.VARIABLE) {
                output.add(t);
                continue;
            }

            // if the token is an operator (o1) then:
            if (t.getType() == BQLTokenizer.Type.OPERATOR) {
                BQLTokenizer.Token o1 = t;
                while (!stk.isEmpty() && stk.peek().getType() == BQLTokenizer.Type.OPERATOR
                        && ((isLeftAssociative(o1) && precedence(o1) <= precedence(stk.peek()))
                        || (!isLeftAssociative(o1) && precedence(o1) < precedence(stk.peek())))) {
                    output.add(stk.pop());
                }
                stk.push(o1);
                continue;
            }

            // if the token is a right array parenthesis, process the array and put it on the stack
            if (t.getType() == BQLTokenizer.Type.LEFT_BRACKET && t.getText().equalsIgnoreCase("[")) {
                List<String> arr = new ArrayList<>();
                for (int j = i + 1; j < matchingBrackets.get(i); j++) {
                    BQLTokenizer.Token tmp = infix.get(j);
                    if (tmp.getType() != BQLTokenizer.Type.COMMA && tmp.getType() != BQLTokenizer.Type.WHITESPACE)
                        arr.add(infix.get(j).getText());
                }
                i = matchingBrackets.get(i);
                BQLTokenizer.Token arrToken = new BQLTokenizer.Token(arr.toArray(new String[arr.size()]));
                output.add(arrToken);
                continue;
            }

            // if the token is a left parenthesis, push it onto the stack
            if (t.getType() == BQLTokenizer.Type.LEFT_BRACKET) {
                stk.push(t);
                continue;
            }

            // if the token is a right parenthesis
            if (t.getType() == BQLTokenizer.Type.RIGHT_BRACKET) {
                while (!stk.isEmpty() && stk.peek().getType() != BQLTokenizer.Type.LEFT_BRACKET) {
                    output.add(stk.pop());
                }
                if (!stk.isEmpty() && stk.peek().getType() == BQLTokenizer.Type.LEFT_BRACKET) {
                    stk.pop();
                }
                continue;
            }
        }
        while (!stk.isEmpty()) {
            BQLTokenizer.Token t = stk.pop();
            if (t.getType() == BQLTokenizer.Type.LEFT_BRACKET || t.getType() == BQLTokenizer.Type.RIGHT_BRACKET)
                throw new IllegalArgumentException("Mismatched parenthesis in expression");
            output.add(t);
        }
        // return
        return output;
    }

    /**
     * Get the precedence of a certain operator
     * Precedence dictates what gets executed first in ambiguous statements such as "A + B * C"
     *
     * @param token
     * @return
     */
    private static int precedence(BQLTokenizer.Token token) {
        String[] operators = {  // high level
                "*",
                // comparison operators
                ">",
                ">=",
                "<",
                "<=",
                // equality operators
                "==",
                "!=",
                // logical operators
                "AND",
                "OR",
                // display operators
                "SELECT",
                "WHERE",
                "SORT"
        };
        for (int i = 0; i < operators.length; i++) {
            if (token.getText().equalsIgnoreCase(operators[i]))
                return operators.length - i;
        }
        return (int) Math.pow(operators.length, 2);
    }

    /**
     * Get whether an operator is left associative or not
     * Associativity helps structuring statements such as "A - B - C - D"
     * which can be interpreted as either "(((A - B) - C) - D)" or "(A - (B - (C - D)))"
     *
     * @param token
     * @return
     */
    private static boolean isLeftAssociative(BQLTokenizer.Token token) {
        return true;
    }

}
