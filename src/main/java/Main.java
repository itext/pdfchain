import chain.IBlockChain;
import chain.MultiChain;
import sign.AbstractExternalSignature;
import sign.DefaultExternalSignature;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Created by Joris Schellekens on 7/10/2017.
 */
public class Main {

    public static void main(String[] args) throws IOException, GeneralSecurityException {

        IBlockChain mc = new MultiChain(
                "http://127.0.0.1",
                4352,
                "chain1",
                "stream1",
                "multichainrpc",
                "BHcXLKwR218R883P6pjiWdBffdMx398im4R8BEwfAxMm");

        AbstractExternalSignature sgn = new DefaultExternalSignature(new File("C:\\Users\\Joris Schellekens\\Downloads\\ks"), "demo", "password");

        File inputFile = new File("C:\\Users\\Joris Schellekens\\Desktop\\pdfs\\30_marked.pdf");

        PdfChain chain = new PdfChain(mc, sgn);

        for (Map<String, Object> docEntry : chain.get("z�L{�Wd=��\u007F\u0010��G�")) {
            for (Map.Entry<String, Object> entry : docEntry.entrySet())
                System.out.println(padRight(entry.getKey(), 32) + " : " + entry.getValue());
            System.out.println("");
        }

    }

    private static String padRight(String s, int len) {
        while (s.length() < len)
            s += " ";
        return s;
    }

}
