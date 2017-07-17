package bql.executor;

import bql.AbstractBQLOperator;
import bql.logical.And;
import bql.relational.*;
import bql.sort.SortBy;
import bql.transform.Select;

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
                AbstractBQLOperator arg0 = (AbstractBQLOperator) tmp.pop();
                AbstractBQLOperator arg1 = (AbstractBQLOperator) tmp.pop();
                tmp.push(new And(arg0, arg1));
            }
            // OR
            else if(t.getText().equalsIgnoreCase("OR"))
            {
                AbstractBQLOperator arg0 = (AbstractBQLOperator) tmp.pop();
                AbstractBQLOperator arg1 = (AbstractBQLOperator) tmp.pop();
                tmp.push(new And(arg0, arg1));
            }
            // >
            else if(t.getText().equalsIgnoreCase(">"))
            {
                BQLTokenizer.Token arg0 = (BQLTokenizer.Token) tmp.pop();
                BQLTokenizer.Token arg1 = (BQLTokenizer.Token) tmp.pop();
                tmp.push(new Greater(arg1.getText(), Double.parseDouble(arg0.getText())));
            }
            // >=
            else if(t.getText().equalsIgnoreCase(">="))
            {
                BQLTokenizer.Token arg0 = (BQLTokenizer.Token) tmp.pop();
                BQLTokenizer.Token arg1 = (BQLTokenizer.Token) tmp.pop();
                tmp.push(new GreaterOrEqual(arg1.getText(), Double.parseDouble(arg0.getText())));
            }
            // <
            else if(t.getText().equalsIgnoreCase("<"))
            {
                BQLTokenizer.Token arg0 = (BQLTokenizer.Token) tmp.pop();
                BQLTokenizer.Token arg1 = (BQLTokenizer.Token) tmp.pop();
                tmp.push(new Smaller(arg1.getText(), Double.parseDouble(arg0.getText())));
            }
            // <=
            else if(t.getText().equalsIgnoreCase("<="))
            {
                BQLTokenizer.Token arg0 = (BQLTokenizer.Token) tmp.pop();
                BQLTokenizer.Token arg1 = (BQLTokenizer.Token) tmp.pop();
                tmp.push(new SmallerOrEqual(arg1.getText(), Double.parseDouble(arg0.getText())));
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
                BQLTokenizer.Token arg1 = (BQLTokenizer.Token) tmp.pop();
                AbstractBQLOperator arg0 = (AbstractBQLOperator) tmp.pop();
                tmp.push(new SortBy(arg0, arg1.getText()));
            }
            // SELECT
            else if(t.getText().equalsIgnoreCase("SELECT"))
            {
                AbstractBQLOperator arg1 = (AbstractBQLOperator) tmp.pop();
                BQLTokenizer.Token  arg0 = (BQLTokenizer.Token) tmp.pop();
                tmp.push(new Select(arg1, arg0.getTexts()));
            }
        }
        if(tmp.size() != 1)
            return null;
        return (AbstractBQLOperator) tmp.pop();
    }


}
