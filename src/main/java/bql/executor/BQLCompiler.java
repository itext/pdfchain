package bql.executor;

import java.util.List;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public class BQLCompiler {

    public static void compile(String expression)
    {
        List<BQLTokenizer.Token> tokens = BQLTokenizer.tokenize(expression);
        tokens = ShuntingYard.postfix(tokens);
    }
}
