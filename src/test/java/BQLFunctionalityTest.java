/**
 * Created by Joris Schellekens on 7/11/2017.
 */
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
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pdfchain.PdfChain;
import sign.AbstractExternalSignature;
import sign.DefaultExternalSignature;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Map;

@Category(IntegrationTest.class)
public class BQLFunctionalityTest {

    @BeforeClass
    public static void beforeClass() {}

    @Test
    public void queryBlockChainTest() throws IOException, GeneralSecurityException {
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
        Executor exe = new Executor(mc);

        // execute query
        boolean isEmpty = exe.execute(op).isEmpty();
        Assert.assertFalse(isEmpty);
    }
}
