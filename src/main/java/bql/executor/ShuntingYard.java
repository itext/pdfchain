package bql.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public class ShuntingYard {

    public static List<BQLTokenizer.Token> postfix(List<BQLTokenizer.Token> infix)
    {
        Stack<BQLTokenizer.Token> stk = new Stack<>();
        List<BQLTokenizer.Token> output = new ArrayList<>();

        for (int i = 0; i < infix.size(); i++) {
            BQLTokenizer.Token t = infix.get(i);

            if(t.getType() == BQLTokenizer.Type.WHITESPACE)
                continue;

            // if the token is a number or literal, or variable, push it to the output queue
            if (t.getType() == BQLTokenizer.Type.NUMBER || t.getType() == BQLTokenizer.Type.STRING) {
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
            }
        }
        while(!stk.isEmpty())
        {
            BQLTokenizer.Token t = stk.pop();
            if(t.getType() == BQLTokenizer.Type.LEFT_BRACKET || t.getType() == BQLTokenizer.Type.RIGHT_BRACKET)
                throw new IllegalArgumentException("Mismatched parenthesis");
            output.add(t);
        }
        // return
        return output;
    }

    private static int precedence(BQLTokenizer.Token token)
    {
        String[] operators = {  // high level
                                "SORT",
                                "SELECT",
                                "WHERE",
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
                                "OR"};
        for(int i=0;i<operators.length;i++)
        {
            if(token.getText().equalsIgnoreCase(operators[i]))
                return operators.length - i;
        }
        return (int) Math.pow(operators.length, 2);
    }

    private static boolean isLeftAssociative(BQLTokenizer.Token token)
    {
        return true;
    }

}
