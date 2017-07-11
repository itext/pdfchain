import blockchain.IBlockChain;
import blockchain.MultiChain;
import blockchain.Record;
import bql.AbstractBQLOperator;
import bql.executor.Executor;
import bql.logical.And;
import bql.logical.Or;
import bql.relational.EqualID;
import bql.relational.Greater;
import bql.relational.Smaller;
import bql.sort.SortBy;
import bql.transform.Select;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by Joris Schellekens on 7/11/2017.
 */
public class BQLMain {

    public static void main(String[] args) {
        IBlockChain mc = new MultiChain(
                "http://127.0.0.1",
                4352,
                "chain1",
                "stream1",
                "multichainrpc",
                "BHcXLKwR218R883P6pjiWdBffdMx398im4R8BEwfAxMm");

        AbstractBQLOperator op = new SortBy(new Select(
                                                new And(
                                                    new Or(
                                                        new Greater("confirmations", 10),
                                                        new Smaller("confirmations", 5)
                                                    ),
                                                    new EqualID("z�L{�Wd=��\u007F\u0010��G�")
                                                ),
                                                new String[]{"id1", "id2", "blocktime", "confirmations"}
                                            ),
                                            "confirmations");

        Executor exe = new Executor(mc);
        Collection<Record> resultSet = exe.execute(op);

        // print
        for (Map<String, Object> docEntry : resultSet) {
            for (Map.Entry<String, Object> entry : docEntry.entrySet())
                System.out.println(padRight(entry.getKey(), 32) + " : " + entry.getValue());
            System.out.println("");
        }
    }

    /**
     * Utility function for pretty printing
     *
     * @param s
     * @param len
     * @return
     */
    private static String padRight(String s, int len) {
        while (s.length() < len)
            s += " ";
        return s;
    }

}
