package bql.executor;

import bql.AbstractBQLOperator;
import bql.logical.And;
import bql.logical.Or;
import bql.relational.*;
import bql.sort.SortBy;
import bql.transform.Select;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.List;
import java.util.Stack;

/**
 * Compiler for BQL
 */
public class BQLCompiler {

    public static AbstractBQLOperator compile(String expression)
    {
        List<BQLTokenizer.Token> tokens = BQLTokenizer.tokenize(expression);

        tokens = ShuntingYard.postfix(tokens);
        Stack<Object> tmp = new Stack<>();
        for(int i=0;i<tokens.size();i++)
        {
            BQLTokenizer.Token t = tokens.get(i);
            if(t.getType() != BQLTokenizer.Type.OPERATOR) {
                tmp.push(t);
                continue;
            }
            // AND
            if(t.getText().equalsIgnoreCase("AND"))
            {
                if(tmp.size() < 2)
                    throw new IllegalArgumentException("Not enough arguments for operator AND");
               tmp.push(buildAnd(tmp.pop(), tmp.pop()));
            }
            // OR
            else if(t.getText().equalsIgnoreCase("OR"))
            {
                if(tmp.size() < 2)
                    throw new IllegalArgumentException("Not enough arguments for operator OR");
                tmp.push(buildOr(tmp.pop(), tmp.pop()));
            }
            // >
            else if(t.getText().equalsIgnoreCase(">"))
            {
                if(tmp.size() < 2)
                    throw new IllegalArgumentException("Not enough arguments for operator >");
                tmp.push(buildGreater(tmp.pop(), tmp.pop()));
            }
            // >=
            else if(t.getText().equalsIgnoreCase(">="))
            {
                if(tmp.size() < 2)
                    throw new IllegalArgumentException("Not enough arguments for operator >=");
              tmp.push(buildGreaterOrEqual(tmp.pop(), tmp.pop()));
            }
            // <
            else if(t.getText().equalsIgnoreCase("<"))
            {
                if(tmp.size() < 2)
                    throw new IllegalArgumentException("Not enough arguments for operator <");
                tmp.push(buildSmaller(tmp.pop(), tmp.pop()));
            }
            // <=
            else if(t.getText().equalsIgnoreCase("<="))
            {
                if(tmp.size() < 2)
                    throw new IllegalArgumentException("Not enough arguments for operator <=");
                tmp.push(buildSmallerOrEqual(tmp.pop(), tmp.pop()));
            }
            // ==
            else if(t.getText().equalsIgnoreCase("=="))
            {
                BQLTokenizer.Token arg1 = (BQLTokenizer.Token) tmp.pop();
                BQLTokenizer.Token arg0 = (BQLTokenizer.Token) tmp.pop();
                Object val = null;
                if(arg1.getType() == BQLTokenizer.Type.STRING)
                    val = arg1.getText().substring(1, arg1.getText().length() - 1);
                else if(arg1.getType() == BQLTokenizer.Type.NUMBER)
                    val = Double.parseDouble(arg1.getText());
                tmp.push(new Equal(arg0.getText(), val));
            }
            // !=
            else if(t.getText().equalsIgnoreCase("!="))
            {
                BQLTokenizer.Token arg1 = (BQLTokenizer.Token) tmp.pop();
                BQLTokenizer.Token arg0 = (BQLTokenizer.Token) tmp.pop();
                Object val = null;
                if(arg1.getType() == BQLTokenizer.Type.STRING)
                    val = arg1.getText().substring(1, arg1.getText().length() - 1);
                else if(arg1.getType() == BQLTokenizer.Type.NUMBER)
                    val = Double.parseDouble(arg1.getText());
                tmp.push(new Unequal(arg0.getText(), val));
            }
            // SORT
            else if(t.getText().equalsIgnoreCase("SORT"))
            {
                if(tmp.size() < 2)
                    throw new IllegalArgumentException("Not enough arguments for operator SORT");
                tmp.push(buildSort(tmp.pop(), tmp.pop()));
            }
            // SELECT
            else if(t.getText().equalsIgnoreCase("SELECT"))
            {
                if(tmp.size() < 2)
                    throw new IllegalArgumentException("Not enough arguments for operator SELECT");
                tmp.push(buildSelect(tmp.pop(), tmp.pop()));
            }
        }
        if(tmp.size() != 1)
            throw new IllegalArgumentException("Invalid input '" + expression + "'");
        return (AbstractBQLOperator) tmp.pop();
    }

    private static AbstractBQLOperator buildSelect(Object arg0, Object arg1)
    {
        if(!isOperator(arg0) || !isArray(arg1))
            throw new IllegalArgumentException("Invalid argument for operator SELECT");
        return new Select((AbstractBQLOperator) arg0, ((BQLTokenizer.Token) arg1).getTexts());
    }

    private static AbstractBQLOperator buildSort(Object arg0, Object arg1)
    {
        if(!isVariable(arg0) || !isOperator(arg1))
            throw new IllegalArgumentException("Invalid argument for operator SORT");
        return new SortBy((AbstractBQLOperator) arg1, ((BQLTokenizer.Token) arg0).getText());
    }

    private static AbstractBQLOperator buildAnd(Object arg0, Object arg1)
    {
        if(!isOperator(arg0) || !isOperator(arg1))
            throw new IllegalArgumentException("Invalid argument for operator AND");
        return new And((AbstractBQLOperator) arg0, (AbstractBQLOperator) arg1);
    }

    private static AbstractBQLOperator buildOr(Object arg0, Object arg1)
    {
        if(!isOperator(arg0) || !isOperator(arg1))
            throw new IllegalArgumentException("Invalid argument for operator OR");
        return new Or((AbstractBQLOperator) arg0, (AbstractBQLOperator) arg1);
    }

    private static AbstractBQLOperator buildGreater(Object arg0, Object arg1)
    {
        if(!isNumber(arg0) || !isVariable(arg1))
            throw new IllegalArgumentException("Invalid argument for operator >");
        return new Greater(((BQLTokenizer.Token) arg1).getText(),
                            Double.parseDouble(((BQLTokenizer.Token) arg0).getText()));
    }

    private static AbstractBQLOperator buildSmaller(Object arg0, Object arg1)
    {
        if(!isNumber(arg0) || !isVariable(arg1))
            throw new IllegalArgumentException("Invalid argument for operator <");
        return new Smaller(((BQLTokenizer.Token) arg1).getText(),
                Double.parseDouble(((BQLTokenizer.Token) arg0).getText()));
    }

    private static AbstractBQLOperator buildGreaterOrEqual(Object arg0, Object arg1)
    {
        if(!isNumber(arg0) || !isVariable(arg1))
            throw new IllegalArgumentException("Invalid argument for operator >=");
        return new GreaterOrEqual(((BQLTokenizer.Token) arg1).getText(),
                Double.parseDouble(((BQLTokenizer.Token) arg0).getText()));
    }

    private static AbstractBQLOperator buildSmallerOrEqual(Object arg0, Object arg1)
    {
        if(!isNumber(arg0) || !isVariable(arg1))
            throw new IllegalArgumentException("Invalid argument for operator <=");
        return new SmallerOrEqual(((BQLTokenizer.Token) arg1).getText(),
                Double.parseDouble(((BQLTokenizer.Token) arg0).getText()));
    }

    private static boolean isVariable(Object o)
    {
        return (o instanceof BQLTokenizer.Token) && ((BQLTokenizer.Token) o).getType() == BQLTokenizer.Type.VARIABLE;
    }

    private static boolean isNumber(Object o)
    {
        return (o instanceof BQLTokenizer.Token) && ((BQLTokenizer.Token) o).getType() == BQLTokenizer.Type.NUMBER;
    }

    private static boolean isString(Object o)
    {
        return (o instanceof BQLTokenizer.Token) && ((BQLTokenizer.Token) o).getType() == BQLTokenizer.Type.STRING;
    }

    private static boolean isArray(Object o)
    {
        return (o instanceof BQLTokenizer.Token) && ((BQLTokenizer.Token) o).getType() == BQLTokenizer.Type.ARRAY;
    }

    private static boolean isOperator(Object o)
    {
        return o instanceof AbstractBQLOperator;
    }

}
