import bql.executor.BQLCompiler;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public class BQLCompMain {

    public static void main(String[] args)
    {
        String stm = "ID1 == '123456' AND CONFIRMATIONS > 10";
        BQLCompiler.compile(stm);
    }
}
