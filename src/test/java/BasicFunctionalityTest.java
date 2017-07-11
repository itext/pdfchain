/**
 * Created by Joris Schellekens on 7/11/2017.
 */
import blockchain.IBlockChain;
import blockchain.MultiChain;
import com.itextpdf.kernel.xmp.XMPException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import pdfchain.PdfChain;
import sign.AbstractExternalSignature;
import sign.DefaultExternalSignature;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Map;

@Category(IntegrationTest.class)
public class BasicFunctionalityTest {

    @BeforeClass
    public static void beforeClass() {}

    @Test
    public void putOnChainTest() throws IOException, GeneralSecurityException {
        IBlockChain mc = new MultiChain(
                "http://127.0.0.1",
                4352,
                "chain1",
                "stream1",
                "multichainrpc",
                "BHcXLKwR218R883P6pjiWdBffdMx398im4R8BEwfAxMm");

        InputStream keystoreInputStream = BasicFunctionalityTest.class.getClassLoader().getResourceAsStream("ks");
        InputStream inputFileStream = BasicFunctionalityTest.class.getClassLoader().getResourceAsStream("input.pdf");

        AbstractExternalSignature sgn = new DefaultExternalSignature(keystoreInputStream, "demo", "password");

        PdfChain chain = new PdfChain(mc, sgn);

        // put a document on the chain
        boolean wasAdded = chain.put(inputFileStream);
        Assert.assertTrue(wasAdded);

        // check whether the chain now contains this value
        boolean isEmpty = chain.get("z�L{�Wd=��\u007F\u0010��G�").isEmpty();
        Assert.assertFalse(isEmpty);
    }
}
