/**
 * Created by Joris Schellekens on 7/11/2017.
 */

import blockchain.IBlockChain;
import blockchain.MultiChain;
import blockchain.Record;
import bql.AbstractBQLOperator;
import bql.executor.BQLCompiler;
import bql.executor.BQLExecutor;
import bql.logical.And;
import bql.logical.Or;
import bql.relational.EqualID;
import bql.relational.Greater;
import bql.relational.Smaller;
import bql.sort.SortBy;
import bql.transform.Select;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;

@Category(IntegrationTest.class)
public class BQLFunctionalityTest {

    @BeforeClass
    public static void beforeClass() {
    }

    @Test
    public void queryBlockChainTestA() throws IOException, GeneralSecurityException {
        IBlockChain mc = new MultiChain(
                "http://127.0.0.1",
                4352,
                "chain1",
                "stream1",
                "multichainrpc",
                "BHcXLKwR218R883P6pjiWdBffdMx398im4R8BEwfAxMm");

        // build query
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

        // build executor
        BQLExecutor exe = new BQLExecutor(mc);

        // execute query
        boolean isEmpty = exe.execute(op).isEmpty();
        Assert.assertFalse(isEmpty);
    }

    @Test
    public void queryBlockChainTestB() throws IOException, GeneralSecurityException {
        IBlockChain mc = new MultiChain(
                "http://127.0.0.1",
                4352,
                "chain1",
                "stream1",
                "multichainrpc",
                "BHcXLKwR218R883P6pjiWdBffdMx398im4R8BEwfAxMm");

        // build query
        AbstractBQLOperator op = BQLCompiler.compile("SELECT [id1, id2, confirmations,hsh]( confirmations > 10 AND confirmations < 50 ) SORT confirmations");

        // build executor
        BQLExecutor exe = new BQLExecutor(mc);

        // execute query
        Collection<Record> resultSet = exe.execute(op);
        Assert.assertFalse(resultSet.isEmpty());
    }
}
